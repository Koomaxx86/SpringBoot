import React, { useContext } from 'react';
import './JoinForm.css';
import { LoginContext } from '../../contexts/LoginContextProvider';

const JoinForm = ({ join }) => {
  const { login } = useContext(LoginContext);

  const onJoin = (e) => {
    e.preventDefault();
    const userId = e.target.username.value;
    const userPw = e.target.password.value;
    const name = e.target.name.value;
    const email = e.target.email.value;

    console.log(userId, userPw, name, email);

    join( { userId, userPw, name, email } );
  };

  return (
    <div className="form">
      <h2 className="login-title">Join</h2>

      <form className="login-form" onSubmit={(e) => onJoin(e)}>
        <div>
          <label htmlFor="username">Username</label>
          <input
            id="username"
            type="text"
            placeholder="Username"
            name="username"
            autoComplete="username"
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            placeholder="Password"
            name="password"
            autoComplete="current-password"
            required
          />
        </div>
        <div>
          <label htmlFor="name">Name</label>
          <input
            id="name"
            type="text"
            placeholder="Name"
            name="name"
            autoComplete="name"
            required
          />
        </div>
        <div>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            placeholder="Email"
            name="email"
            autoComplete="email"
            required
          />
        </div>

        <button className="btn btn--form btn-login" type="submit">
          Join
        </button>
      </form>
    </div>
  );
};

export default JoinForm;
