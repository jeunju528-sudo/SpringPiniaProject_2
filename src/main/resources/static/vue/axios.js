const api = axios.create({
	// baseURL:'http://AWS주소', localhost는 자체적으로 인식해서 안적어도 됨
	timeout: 50000 // 대기상태 5분
})