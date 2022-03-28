package om.beyond.project_toy_revert.datas


// 서버가 주는 응답의 제일 기본 형태인 code, message, data를 파싱해주는 클래스
// 레트로핏과 연계하면, 파싱이 자동 진행됨

class BasicResponse(
    val code:Int,


    val username:String,
    val email:String,
    val password1:String,
    val password2:String,
    val password:String,
) {

}