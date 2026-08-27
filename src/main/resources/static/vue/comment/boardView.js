const {createPinia} = Pinia
const {ref, onMounted, onUnmounted, createApp} = Vue

const commentApp = createApp({
	setup(){
		const store = useBoardStore()
		const msgRef = ref(null)
		
		onMounted(()=>{
			store.sessionId = SESSION_ID
			store.boardCommentListData(BOARDNO)
			store.connect(SESSION_ID)
		})
		
		onUnmounted(()=>{
			store.disconnect()
		})
		
		return {
			store,
			msgRef
		}
	}
})

commentApp.use(createPinia()).mount('#comment')