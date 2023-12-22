import React from 'react'
import Header from '../components/Header/Header'
import LoginContextConsumer from '../contexts/LoginContextConsumer'

const Home = () => {
  return (
    <>
        <Header />
        <div className="container">
            <h1>Home</h1>
            <hr />
            <h2>메인 페이지</h2>
            <LoginContextConsumer />
        </div>
    </>
  )
}

export default Home