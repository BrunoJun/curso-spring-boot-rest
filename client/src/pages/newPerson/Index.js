import React from 'react';
import logo from '../../assets/logo.svg'
import { Link } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi'

import './styles.css';


export default function NewPerson(){

    return(

        <div className='new-person-container'>
            <div className='content'>
                <section className='form'>
                    <img src={logo} alt='logo image'/>
                    <h1>Add new person</h1>
                    <p>Enter the person information and click on 'Add'</p>
                    <Link className='back-link' to='/person'>
                        <FiArrowLeft size={16} color='#251fc5'/>
                        Home
                    </Link>
                </section>

                <form>
                    <input placeholder='First Name'/>
                    <input placeholder='Last Name'/>
                    <input placeholder='Address'/>
                    <input placeholder='Gender'/>

                    <button className='button' type='submit'>Add</button>
                </form>
            </div>
        </div>
    );
}