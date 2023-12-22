import React, { useContext } from 'react';
import Header from '../components/Header/Header';
import { LoginContext } from '../contexts/LoginContextProvider';

const Home = () => {

    const { isLogin, logout, userInfo } = useContext(LoginContext);

    return (
        <>
            <Header />
            <div className='container'>
                <h1>Home</h1>
                <hr/>
                { isLogin && 
                <>
                    <h3><span style={ {color: 'blue' }} >{userInfo.userId}</span> 님 환영합니다.</h3>
                    <hr />
                </>
                }

                <img src="/img/main.jpg" alt="메인 이미지" style={ {width: '100%',} }/>

            </div>
        </>
    )
}

export default Home