import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as Swal from '../apis/alert';
import * as auth from '../apis/auth';
import api from '../apis/api';


export const LoginContext = React.createContext();
LoginContext.displayName = 'LoginContextName'

const LoginContextProvider = ({ children }) => {

  // 페이지 이동
  const navigate = useNavigate()

  // 로그인 여부
  const [isLogin, setIsLogin] = useState(false);

  // 유저 정보
  const [userInfo, setUserInfo] = useState({});

  // 권한 정보
  const [roles, setRoles] = useState({ isUser : false, isAdmin : false });  
  
  // 아이디 저장
  const [rememberUserId, setRememberUserId] = useState()


  /**
   * 💍✅ 로그인 체크
   */
  const loginCheck = async (isAuthPage=false) => {

    const accessToken = Cookies.get("accessToken")
    console.log(`accessToken : ${accessToken}`);
    let response
    let data

    // JWT (accessToken) 이 없음 - 💍in🍪 ❌
    // JWT❌ AND 인증필요❌
    if( !accessToken ) {
      console.log(`쿠키에 JWT(accessToken) 이 없음`);
      logoutSetting()
      return
    }

    //  JWT❌ AND 인증필요⭕
    if( !accessToken && isAuthPage ) {
      navigate("/login")
    }

    // JWT (accessToken) 이 있음 - 💍in🍪 ⭕
    console.log(`쿠키에 JWT(accessToken) 이 저장되어 있음`);
    api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

    try {
      response = await auth.info()
    } catch (error) {
      console.error(`error : ${error}`)
      console.log(`status : ${response.status}`)
      return
    }

    // 응답실패 시, 세팅 ❌
    if( !response ) return

    console.log(`JWT (accessToken) 토큰 으로 사용자 인증 정보 요청 성공!`);

    data = response.data
    console.log(`data : ${data}` );
    

    // 인증 실패
    if( data == 'UNAUTHORIZED' || response.status == 401 ) {
      // 인증이 안되어 있는 경우,
      // 로그인 페이지로 이동 OR refresh token 고려
      // 로그인이 필요한 페이지인 경우, 로그인 페이지로 이동
      // navigate("/login")
      console.error(`JWT (accessToken) 이 만료되었거나 인증에 실패하였습니다.`);
      return
    }

    // ✅ 인증 성공 
    // 정보 세팅
    loginSetting(data, accessToken)

  }


  /**
   * 🔐 로그인
   */
  const login = async (username, password, rememberId) => {

    console.log(`username : ${username}`);
    console.log(`password : ${password}`);
    console.log(`rememberId : ${rememberId}`);

    // 아이디 저장
    if( rememberId ) Cookies.set("rememberId", username)
    else Cookies.remove("rememberId")

    try {
      const response = await auth.login(username, password)
      const data = response.data
      const status = response.status
      const headers = response.headers
      const authorization = headers.authorization
      const accessToken = authorization.replace("Bearer ", "")    // 💍JWT

      console.log(`data : ${data}`)
      console.log(`status : ${status}`)
      console.log(`headers : ${headers}`)
      console.log(`jwt : ${accessToken}`)

      // ✅ 로그인 성공
      if( status === 200 ) {
        Cookies.set("accessToken", accessToken);
        
        // 로그인 체크 ➡ 로그인 세팅
        loginCheck()

        // 페이지 이동 ➡ "/" (메인)
        // TODO : 메인 화면으로 꼭 이동할 필요가 있을까?
        Swal.alert("로그인 성공", "메인 화면으로 이동합니다.", "success", () => { navigate("/") })
      }

      
    } catch (error) {
      console.error(`error : ${error}`)
      // 로그인 실패
      // - 아이디 또는 비밀번호가 일치하지 않습니다.
      Swal.alert("로그인 실패", "아이디 또는 비밀번호가 일치하지 않습니다.", "error" )
    }




  }


  /**
   * 🔓 로그아웃
   */
  const logout = (force=false) => {

    // comfrim 없이 강제 로그아웃
    if( force ) {
      // 로그아웃 세팅
      logoutSetting()
  
      // 페이지 이동 ➡ "/" (메인)
      navigate("/")
      return
    }

    Swal.confirm("로그아웃하시겠습니까?", "로그아웃을 진행합니다.", "warning", 
      (result) => {
        if( result.isConfirmed ) {
          // 로그아웃 세팅
          logoutSetting()

          // 페이지 이동 ➡ "/" (메인)
          navigate("/")
        }
    })

  }

  // 로그인 세팅
  const loginSetting = (userData, accessToken) => {

    const { no, userId, authList } = userData
    const roleList = authList.map((auth) => auth.auth)
    console.log(`no : ${no}`)
    console.log(`userId : ${userId}`)
    console.log(`authList : ${authList}`)
    console.log(`authList : ${authList.length}`)
    console.log(`roleList : ${roleList}`)

    // 💍 ➡ 🍪
    // JWT 토큰 을 header 저장
    api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

    // 로그인 여부
    setIsLogin(true)

    // 유저정보 세팅
    const updatedUserInfo = { no, userId, roleList }
    setUserInfo(updatedUserInfo)

    // 권한정보 세팅 
    const updatedRoles = { isUser : false, isAdmin : false }
    roleList.forEach((role) => {
      if( role == 'ROLE_USER') updatedRoles.isUser = true
      if( role == 'ROLE_ADMIN') updatedRoles.isAdmin = true
    });
    setRoles(updatedRoles)
    

  }

  // 로그아웃 세팅
  const logoutSetting = () => {
    // 상태 비우기
    setIsLogin(false)
    setUserInfo(null)
    setRoles(null)
    // 🍪 쿠키 지우기
    Cookies.remove("accessToken")
    api.defaults.headers.common.Authorization = undefined;
  }


  // 로그인 체크
  // 쿠키에서 accessToken 가져와서 확인
    // 💍 ⭕ : 토큰 있으면, /user/info 로 요청보내서 userInfo state 등록
    // 💍 ❌ : 토큰 없으면, isLogin : false
    //          ➡ 로그인이 필요한 페이지라면, /login 페이지로 navigate()

  // 로그인 요청
  // username, password [POST] /login 요청
    // 💍 ⭕ : 응답헤더(Authrization)에 토큰 있으면, 
    //          1. 쿠키에 { accessToken : jwt } 등록    (Bearer 제외)
    //          2. axios commont header  에 등록        (client.defaults.headers.common.Authorization = `Bearer ${jwt}`;)
    //          3. isLogin : true
    //          4. 권한 정보 : true

  // 로그인 아웃
    // 1. isLogin : false
    // 2. userInfo : null
    // 3. roles : null

  


  useEffect(() => {

    // 로그인 체크
    loginCheck()


    
  }, []);

  return (
    <LoginContext.Provider value={ {isLogin, userInfo, roles, loginCheck, login, logout } }>
      {children}
    </LoginContext.Provider>
  );
};

export default LoginContextProvider;
