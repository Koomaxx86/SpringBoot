import React, { useContext, useEffect, useState } from 'react';
import Header from '../components/Header/Header';
import UserForm from '../components/User/UserForm'
import * as auth from '../apis/auth';
import * as Swal from '../apis/alert';
import { LoginContext } from '../contexts/LoginContextProvider';

const Home = () => {

    const [userInfo, setUserInfo] = useState()
    const { logout } = useContext(LoginContext)

    // 사용자 정보 조회 
    const getUserInfo = async () => {
        const response  = await auth.info()
        const data = response.data
        console.log(`getUserInfo :: `);
        console.log(data);

        setUserInfo(data)
    }

    // 회원 정보 수정
    const updateUser = async ( form ) => {
        console.log(form);
    
        let response
        let data
        try {
            response = await auth.update(form)
        } catch (error) {
            console.error(`${error}`)
            console.error(`회원수정 중 에러가 발생하였습니다.`);
            return
        }

        data = response.data
        const status = response.status
        console.log(`data : ${data}`);
        console.log(`status : ${status}`);

        if( status == 200 ) { 
            console.log(`회원수정 성공!`);
            Swal.alert("회원수정 성공", "로그아웃 후, 다시 로그인해주세요.", "success", () => { logout(true) })
        }
        else { 
            console.log(`회원수정 실패!`);
            Swal.alert("회원수정 실패", "회원수정에 실패하였습니다.", "error" )
        }
    }

    // 회원 탈퇴
    const deleteUser = async (userId) => {
        console.log(userId);
    
        let response
        let data
        try {
            response = await auth.remove(userId)
        } catch (error) {
            console.error(`${error}`)
            console.error(`회원삭제 중 에러가 발생하였습니다.`);
            return
        }

        data = response.data
        const status = response.status
        console.log(`data : ${data}`);
        console.log(`status : ${status}`);

        if( status == 200 ) { 
            console.log(`회원탈퇴 성공!`);
            Swal.alert("회원탈퇴 성공", "그동안 감사했습니다:)", "success", () => { logout(true) })
        }
        else { 
            console.log(`회원탈퇴 실패!`);
            Swal.alert("회원탈퇴 실패", "회원탈퇴에 실패하였습니다.", "error" )
        }
    }

    useEffect( () => {
        getUserInfo()
    }, [])

    return (
        <>
            <Header />
            <div className='container'>
                <UserForm userInfo={userInfo} updateUser={updateUser} deleteUser={deleteUser} />
            </div>
        </>
    )
}

export default Home