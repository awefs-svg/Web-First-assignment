// TODO FE-01~04: 공통 API 명세와 구현 체크리스트를 보고 완성하세요.
let session = { accessToken: "", user: null };
export const getSession = () => session;
export function clearSession() {
  session = { accessToken: "", user: null };
}

function authorizationHeaders() {
  // TODO FE-02: 메모리에 보관한 토큰을 Bearer 헤더로 보내세요.
  if(!session.accessToken){
      return {};
  }
  return { Authorization : `Bearer ${session.accessToken}`};
} // ${} : Java Script의 템플릿 리터럴 문법

export async function request(path, options = {}) {
  const response = await fetch(`/api${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...authorizationHeaders(),
      ...options.headers,
    },
  });
  const data = response.status === 204 ? null : await response.json();
    if (!response.ok) {
      if (response.status === 401 ){ // 401 -> 메모리의 로그인 정보를 초기화
        clearSession();
      }
    // TODO FE-03: 401이면 메모리의 로그인 상태도 해제하세요.
    const error = new Error(
      data.message || data.code || `HTTP ${response.status}`,
    );
    error.status = response.status;
    error.code = data.code;
    throw error;
  }
  return data;
}

export async function login(email, password) {
  // TODO FE-01: POST /auth/login → 응답의 토큰과 사용자 정보를 session에 저장.
  // request()를 이용할 수 있습니다. 성공하면 user를 반환하세요.
//  throw new Error("로그인 연결은 학생 구현 항목입니다. FE-01을 완성하세요.");
 
  // 로그인 요청 보내기
  const data = await request("/auth/login", {method: "POST", body:JSON.stringify
    ({email : email, password : password,})});
    // request("/auth/login",~) : 기존 request() 함수가 /api를 붙여서 /api/auth/login으로 요청
    // method : "POST" 서버에 로그인 정보를 전송
    // JSON.stringify(~) : 이메일과 비밀번호를 JSON 문자열로 변환
    // await : 서버가 응답할 때까지 기다림
    // data : 서버에서 반환한 로그인 결과를 저장

  //로그인 정보 저장하기
    session = {
      accessToken : data.accessToken,
      user: data.user
    };

    return data.user;
}

  export function canEdit(post) {
  // TODO FE-04: 로그인 사용자와 작성자를 비교하세요. 서버 검사도 반드시 필요합니다.
  if (!session.user || !post){
    return false;
  }
  
  return String(session.user.id) === String(post.authorId); // 시작 코드에서는 CRUD를 체험할 수 있도록 모두 표시합니다.
}
export const api = {
  list: () => request("/posts"),
  detail: (id) => request(`/posts/${id}`),
  create: (input) =>
    request("/posts", { method: "POST", body: JSON.stringify(input) }),
  update: (id, input) =>
    request(`/posts/${id}`, { method: "PUT", body: JSON.stringify(input) }),
  remove: (id) => request(`/posts/${id}`, { method: "DELETE" }),
  me: () => request("/me"),
};
