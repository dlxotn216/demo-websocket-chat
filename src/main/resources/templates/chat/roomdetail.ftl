<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div>
        <h2>{{room.name}}</h2>
    </div>
    <div class="input-group">
        <template v-if="imageMode">
            <template v-if="!image">
                <input type="file" name="image" class="form-control" @change="onFileChange">
            </template>
            <template v-else>
                <img :src="image"/>
                <button @click="removeImage">Remove image</button>
            </template>
        </template>
        <template v-else>
            <div class="input-group-prepend">
                <label class="input-group-text">내용</label>
            </div>
            <input type="text" class="form-control" ref="message" v-model="message" v-on:keypress.enter="sendMessage">
        </template>
        <div class="input-group-append">
            <button v-if="imageMode" class="btn btn-primary" type="button" @click="sendImage">업로드</button>
            <button v-if="!imageMode" class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
            <button v-if="!imageMode" class="btn btn-primary" type="button" @click="toggleImage">이미지</button>
        </div>
    </div>
    <ul class="list-group">
        <template v-for="message in messages">
            <template v-if="message.sender === sender">
                <li class="list-group-item" style="text-align: right">
                    <template v-if="message.type === 'FILE'">
                        <a>{{message.message}}</a> <br />
                        <img v-bind:src="imageSrc(message)"/>
                    </template>
                    <template v-else>
                        {{message.message}}
                    </template>
                </li>
            </template>
            <template v-else>
                <li class="list-group-item">
                    <template v-if="message.type === 'FILE'">
                        <a>[{{message.sender}}] - {{message.message}}</a> <br />
                        <img v-bind:src="imageSrc(message)"/>
                    </template>
                    <template v-else>
                        [{{message.sender}}] - {{message.message}}
                    </template>
                </li>
            </template>
        </template>
    </ul>
    <div></div>
</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    //alert(document.title);
    // websocket &amp; stomp initialize
    const sock = new SockJS("/ws-stomp");
    const ws = Stomp.over(sock);
    const reconnect = 0;
    // vue.js
    const vm = new Vue({
        el: '#app',
        data: {
            roomId: '',
            room: {},
            sender: '',
            message: '',
            messages: [],
            image: '',
            file: '',
            imageMode: false
        },
        created() {
            this.roomId = sessionStorage.getItem('wschat.roomId');
            this.sender = sessionStorage.getItem('wschat.sender');
            this.init();
            this.findRoom();
        },
        methods: {
            init: function () {
                axios.get('/chat/room/' + this.roomId).then(response => {
                    this.room = response.data;
                });
            },
            findRoom: function () {
                axios.get('/chat/room/' + this.roomId + '/messages').then(response => {
                    console.log(response.data);
                    this.messages = response.data;
                });
            },
            toggleImage: function () {
                this.imageMode = !this.imageMode;
            },
            onFileChange(e) {
                const files = e.target.files || e.dataTransfer.files;
                if (!files.length)
                    return;

                this.file = files[0];
                this.createImage(files[0]);
            },
            createImage(file) {
                const image = new Image();
                const reader = new FileReader();
                const vm = this;

                reader.onload = (e) => {
                    vm.image = e.target.result;
                };
                reader.readAsDataURL(file);
            },
            removeImage: function (e) {
                this.image = '';
            },
            sendMessage: function () {
                if (!this.message) {
                    return;
                }

                this.publishMessage(JSON.stringify({
                    type: 'TEXT',
                    roomId: this.roomId,
                    sender: this.sender,
                    message: this.message
                }));
                this.message = '';
                this.$refs.message.$el.focus();
            },
            sendImage: function () {
                if (!this.file) {
                    return;
                }

                const URL = '/uploads';
                let data = new FormData();
                data.append('image', this.file);

                let config = {
                    header: {
                        'Content-Type': 'image/png'
                    }
                };

                axios.post(
                        URL,
                        data,
                        config
                ).then(response => {
                    console.log('image upload response > ', response);
                    this.removeImage();
                    this.toggleImage();

                    this.publishMessage(JSON.stringify({
                        type: 'FILE',
                        roomId: this.roomId,
                        sender: this.sender,
                        message: response.data.fileName,
                        downloadPath: response.data.fileId
                    }));
                })
            },
            publishMessage: function (message) {
                ws.send("/pub/chat/message", {}, message);
            },
            imageSrc: function (message) {
                return "/downloads/" + message.downloadPath;
            },
            recvMessage: function (recv) {
                this.messages.unshift({
                    "type": recv.type,
                    "sender": recv.sender,
                    "message": recv.message,
                    "downloadPath": recv.downloadPath
                })
            },
            getLineStyles: function (message) {
                return this.sender === message.sender
                        ? 'text-align: right'
                        : 'text-align: left';
            }
        }
    });

    function connect() {
        // pub/sub event
        ws.connect({}, function (frame) {
            ws.subscribe("/sub/chat/room/" + vm.$data.roomId, function (message) {
                const recv = JSON.parse(message.body);
                vm.recvMessage(recv);
            });
            ws.send("/pub/chat/message", {}, JSON.stringify({
                type: 'ENTER',
                roomId: vm.$data.roomId,
                sender: vm.$data.sender
            }));
        }, function (error) {
            if (reconnect++ <= 5) {
                setTimeout(function () {
                    console.log("connection reconnect");
                    sock = new SockJS("/ws-stomp");
                    ws = Stomp.over(sock);
                    connect();
                }, 10 * 1000);
            }
        });
    }
    connect();
</script>
</body>
</html>