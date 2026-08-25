const { defineStore } = Pinia
const { nextTick } = Vue

const useChatStore = defineStore('chat', {

    state: () => ({

        stomp: null,

        users: [],

        messages: [],

        publicMessages: [],

        privateMessages: {},

        currentRoom: 'public',

        loginUser: '',

        chatBodyEl: null,

        msg: ''
    }),

    actions: {

        makeRoomId(user1, user2) {

            return [
                user1,
                user2
            ]
            .sort()
            .join('_')
        },

        getOtherUser(roomId) {

            if (roomId === 'public') {
                return ''
            }

            const users =
                roomId.split('_')

            return users[0] === this.loginUser
                ? users[1]
                : users[0]
        },

        changeRoom(user) {

            if (user === 'public') {

                this.currentRoom = 'public'

                this.messages =
                    this.publicMessages
            }

            else {

                const roomId =
                    this.makeRoomId(
                        this.loginUser,
                        user
                    )

                this.currentRoom =
                    roomId

                if (!this.privateMessages[roomId]) {
                    this.privateMessages[roomId] = []
                }

                this.messages =
                    this.privateMessages[roomId]
            }

            this.scrollToBottom()
        },

        connect() {
            const socket = new SockJS('/chat-ws')

            this.stomp = Stomp.over(socket)
			
			// stomp console.log 제거
            this.stomp.debug = null

            this.stomp.connect(
                {},
                () => {
                    console.log('WebSocket 연결 성공')
					
					// 로그인 유저 목록에 작성할 수 있도록 send
					this.stomp.send(
						'/app/chat/join',{},
						JSON.stringify({})
					)
					
					// 사용자 목록 가져와서 유저 목록에 대입함
                    this.stomp.subscribe(
                        '/topic/users',
                        msg => {
                            const users = JSON.parse(msg.body)
                            this.users = users.filter(
                            	u => u !== this.loginUser
                            )
                        }
                    )
					
					// 전체 채팅목록 구독
                    this.stomp.subscribe(
                        '/topic/chat',
                        msg => {
                            const m = JSON.parse(msg.body)
							
							// 전체 채팅목록 변수에 저장
							this.publicMessages.push(m)

                            if (this.currentRoom ==='public') {
                                this.messages = this.publicMessages
                                this.scrollToBottom()
                            }
                        }
                    )
					
					// 개인 채팅목록 구독
                    this.stomp.subscribe(
                        '/user/queue/chat',

                        msg => {
                            const m = JSON.parse(msg.body)

                            const roomId = this.makeRoomId(m.sender, m.receiver)

                            if (!this.privateMessages[roomId]) {
                                this.privateMessages[roomId] = []
                            }

                            this.privateMessages[roomId].push(m)

                            if (this.currentRoom === roomId) {
								// 방 별로 채팅내용을 담아서 보내줌
                                this.messages = this.privateMessages[roomId]
                                this.scrollToBottom()
                            }
                        }
                    )
					
					// 로그아웃
                    this.stomp.subscribe(
                        '/user/queue/force-disconnect',

                        () => {
                            alert('중복 로그인으로 로그아웃되었습니다.')

                            location.href = '/logout'
                        }
                    )
                },

                error => {
                    console.error('WebSocket 연결 실패',error)
                }
            )
        },

        async scrollToBottom() {
            await nextTick()
            if (this.chatBodyEl) {
                this.chatBodyEl.scrollTop = this.chatBodyEl.scrollHeight
            }
        },
		
		// 전체메세지 발송
        sendPublic(message) {
            this.stomp.send(
                '/app/chat/public',
                {},
                JSON.stringify({
                    message: message
                })
            )
        },
		
		// 개인 메세지 발송
        sendPrivate(to, message) {
            this.stomp.send(
                '/app/chat/private',
                {},
                JSON.stringify({
                    receiver: to,
                    message: message
                })
            )
        },

        send() {
            if (!this.msg.trim()) {
                return
            }
			
			// 전체 메세지
            if (this.currentRoom ==='public') {
                this.sendPublic(this.msg)
            }
			// 개인 메세지
            else {
                const users = this.currentRoom.split('_')  // 방이름 kim_hong : 보낸사람_받는사람
                const receiver =
                    users[0] === this.loginUser? users[1] : users[0]

                this.sendPrivate(receiver,this.msg)
            }
			
			// 보낸 다음에는 메세지 작성 칸 비우기
            this.msg = ''
        }
    }
})