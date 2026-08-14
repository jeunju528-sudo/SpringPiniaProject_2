const {defineStore} = Pinia

// store에서 관리할 state 설정
const initial_state=()=>({
	list:[],
	curpage:1,
	totalpage:0,
	startpage:0,
	endpage:0
})


// store 생성 -> defineStore로 생성
const useFoodStore = defineStore('foodStore',{
	state: initial_state,
	getters:{
		range:(state)=>{
			const arr=[]
			for(let i=state.startpage; i<=state.endpage; i++){
				arr.push(i) // 맨 뒤에 값 저장
			}
			return arr
		}
	},
	actions:{
		async foodListData(){
			try {
                const response = await api.get('/food/list_vue', {
                    params: {
                        page: this.curpage
                    }
                })
                console.log(response.data)
                this.list = response.data.list
                this.curpage = response.data.curpage
                this.totalpage = response.data.totalpage
                this.startpage = response.data.startpage
                this.endpage = response.data.endpage
			}
			catch(error){
				console.log(error.response)
				// 4XX, 5XX 등 에러 코드를 반환했을 때
				if(error.response){
					console.error('HTTP 상태 코드:', error.response.status)
					console.error('응답 데이터:', error.response.data.status)
					console.error('응답 데이터:', error.response.data.message)
				}
				// 요청은 전송되었으나 응답을 못받았을 경우
				else if(error.request){
					
				}
				// 요청 설정 중 에러가 발생했을 경우
				else {
					
				}
			}
			
		},
		move(page){
			this.curpage = page
			this.foodListData()
		}
	}
})


