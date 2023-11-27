import React from 'react';
import './styles.css';

import logo from '../../assets/logo.svg'
import padlock from '../../assets/padlock.png'

export default function Login({}){

    return(

        <div className='login-container'>
            <section className='form'>
                <img src={logo} alt="logo image"/>

                <form>
                    <h1>Access your account</h1>
                    <input placeholder='username'/>
                    <input type='password' placeholder='password'/>

                    <button className='button' type='submit'>Login</button>
                </form>
            </section>

            <img src={padlock} alt="login"/>
        </div>
    )
}