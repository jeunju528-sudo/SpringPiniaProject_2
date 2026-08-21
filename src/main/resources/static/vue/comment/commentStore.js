// defineStore : 새로운 store를 정의할 때 사용
const {defineStore} = Pinia
const useCommentStore = defineStore('comment',{
	state:()=>({
		rList:[],
		curpage:1,
		totalpage:0,
		count:0,
		sessionId:'',
		fno:0,
		msg:'',
		editNo:null,
		editMsg:'' // editMsg:{}
	}),
	getters:{
		
	},
	actions:{
		async commentListData(fno){
			this.fno = fno
			const res = await api.get('/comment/list_vue',{
				params:{
					page:this.curpage,
					fno
				}
			})
			
			console.log(res.data) // data -> Map
			this.rList = res.data.rList
			this.curpage = res.data.curpage
			this.totalpage = res.data.totalpage
			this.count = res.data.count
			
		},
		async commentInsert(msgRef){
			if(this.msg===''){
				msgRef?.focus()
				return
			}
			const res = await api.post('/comment/insert_vue',{
				page:this.curpage,
				fno:this.fno,
				msg:this.msg
			})
			
			console.log(res.data) // data -> Map
			this.rList = res.data.rList
			this.curpage = res.data.curpage
			this.totalpage = res.data.totalpage
			this.count = res.data.count
			this.msg=''
			
		},
		async commentDelete(no){
			const res = await api.delete('/comment/delete_vue',{
				params:{
					page:this.curpage,
					fno:this.fno,
					no:no
				}
			})
			
			console.log(res.data) // data -> Map
			this.rList = res.data.rList
			this.curpage = res.data.curpage
			this.totalpage = res.data.totalpage
			this.count = res.data.count
		},
		toggleUpdate(no, msg){
			this.editNo=this.editNo===no?null:no
			this.editMsg=msg
			// this.editMsg[no]=msg
		},
        async commentUpdate(no) {
            const res = await api.put('/comment/update_vue', {
                no: no,
                fno: this.fno,
                page: this.curpage,
                msg: this.editMsg
            })
            console.log(res.data) // res.data => map
            this.rList = res.data.rList
            this.curpage = res.data.curpage
            this.totalpage = res.data.totalpage
            this.count = res.data.count
            this.editNo = null
        },
		move(page){
			this.curpage=page
			this.commentListData(this.fno)
		}
		
	}
})
