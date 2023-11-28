import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/Api';
import './styles.css';

import logo from '../../assets/logo.svg'
import padlock from '../../assets/padlock.png'

export default function Login(){

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    async function login(e){

        e.preventDefault();

        const data = {

            username,
            password
        };

        try {
            
            const response = await api.post('auth/signin', data);

            localStorage.setItem('username', username)
            localStorage.setItem('accessToken', response.data.accessToken)

            navigate('/person');
        } catch (error) {
            
            alert('Login failed!')
        }
    }

    return(

        <div className='login-container'>
            <section className='form'>
                <img src={logo} alt="logo image"/>

                <form onSubmit={login}>
                    <h1>Access your account</h1>
                    <input placeholder='username' value={username} onChange={e => setUsername(e.target.value)}/>
                    <input type='password' placeholder='password' value={password} onChange={e => setPassword(e.target.value)}/>

                    <button className='button' type='submit'>Login</button>
                </form>
            </section>

            <img src={padlock} alt="login"/>
        </div>
    )
}