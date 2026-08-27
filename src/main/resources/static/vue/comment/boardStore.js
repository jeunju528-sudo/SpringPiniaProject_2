const {defineStore} = Pinia
const { nextTick } = Vue

const useBoardStore = defineStore('board_comment',{
	state:()=>({
		list:[],
		curpage:1,
		totalpage:0,
		board_no:0,
		sessionId:'',
		count:0,
		msg:'',
		stomp:null,
		updateMsg:{},
		updateReplyNo:null,
		replyMsg:{},
		ReplyNo:null,
		reReplyNo:null
	}),
	actions:{
		connect(id){
			const sock = new SockJS('/chat-ws')
			this.stomp = Stomp.over(sock)
			this.stomp.connect({},()=>{
				this.stomp.subscribe('/sub/notice/'+id, msg=>{
					this.showToast(msg.body)
					this.boardCommentListData(this.board_no)
				})
			})
		},
		disconnect(){
			// 연결이 되어있으면
			if(this.stomp && this.stomp.connected){
				this.stomp.disconnect(()=>{
					console.log("STOMP 종료")
				})
			}
		},
		setCommentData(res){
			console.log(res.data)
			this.list = res.data.list
			this.curpage = res.data.curpage
			this.totalpage = res.data.totalpage
			this.count = res.data.count
		},
		async boardCommentListData(board_no){
			this.board_no = board_no
			const res = await api.get('/reply/list_vue',{
				params:{
					page:this.curpage,
					board_no:board_no
				}
			})
			this.setCommentData(res)
		},
		async boardCommentInsert(msgRef){
			if(this.msg == null){
				msgRef?.focus()
				return
			}
			//post 방식에서는 params 쓰면 안됨
			const res = await api.post('/reply/insert_vue', {
				page:this.curpage,
				board_no:this.board_no,
				msg:this.msg
			})
			this.setCommentData(res)
			this.msg=''
		},
		toggleReply(no){
			this.reReplyNo = this.reReplyNo=== no ?null:no
		},
		async boardCommentReplyInsert(no){
			const res = await api.post('/reply/rereply_insert_vue',{
				no:no,
				board_no:this.board_no,
				page:this.curpage,
				msg:this.replyMsg[no]
			})
			this.setCommentData(res)
			this.reReplyNo=null
			this.replyMsg=''
		},
		async showToast(message){
			await nextTick()
			
			const toast = document.getElementById("replyToast")
			const toastMsg = document.getElementById("toastMsg")
			toastMsg.innerText = message
			toast.classList.add("show")
			
			setTimeout(()=>{
				hideToast()
			},5000)
		}
	}
})

function hideToast(){
	const toast = document.getElementById("replyToast")
	toast.classList.remove("show")
}