import React from 'react'

const JoinForm = ({ join }) => {

    const onJoin = (e) => {
        e.preventDefault()      // submit 기본 동작 방지
        const form = e.target
        const userId = form.username.value
        const userPw = form.password.value
        const name = form.name.value
        const email = form.email.value

        console.log(userId, userPw, name, email);

        join( {userId, userPw, name, email} )
    }

  return (
    <div className="form">
        <h2 className="login-title">Join</h2>

        <form className='login-form' onSubmit={ (e) => onJoin(e) }>
            <div>
                <label htmlFor="name">username</label>
                <input type="text"
                        id='username'
                        placeholder='username'
                        name='username'
                        autoComplete='username'
                        required
                />
            </div>

            <div>
                <label htmlFor="password">password</label>
                <input type="password"
                        id='passowrd'
                        placeholder='password'
                        name='password'
                        autoComplete='password'
                        required
                />
            </div>

            <div>
                <label htmlFor="name">Name</label>
                <input type="text"
                        id='name'
                        placeholder='name'
                        name='name'
                        autoComplete='name'
                        required
                />
            </div>

            <div>
                <label htmlFor="name">Email</label>
                <input type="text"
                        id='email'
                        placeholder='email'
                        name='email'
                        autoComplete='email'
                        required
                />
            </div>

            <button type='submit' className='btn btn--form btn-login'>
                Join                
            </button>
        </form>
    </div>
  )
}

export default JoinForm