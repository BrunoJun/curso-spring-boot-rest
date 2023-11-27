import React from 'react';
import { Link } from 'react-router-dom';
import logo from '../../assets/logo.svg';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi'

import './styles.css'


export default function Person(){

    return (
        <div className='person-container'>
            <header>
                <img src={logo} alt='logo image'/>
                <span>Welcome, <strong>Bruno</strong></span>
                <Link className='button' to="/person/new">Add new person</Link>
                <button type='button'>
                    <FiPower size={18} color='#251fc5'/>
                </button>
            </header>

            <h1>Registered people</h1>
            <ul>
                <li>
                    <strong>First name:</strong>
                    <p>Bruno</p>

                    <strong>Last name:</strong>
                    <p>Yamada</p>

                    <strong>Address:</strong>
                    <p>Cotia</p>

                    <strong>Gender:</strong>
                    <p>Male</p>

                    <button type='button'>
                        <FiEdit size={20} color='251fc5'/>
                    </button>

                    <button type='button'>
                        <FiTrash2 size={20} color='251fc5'/>
                    </button>
                </li>

                <li>
                    <strong>First name:</strong>
                    <p>Bruno</p>

                    <strong>Last name:</strong>
                    <p>Yamada</p>

                    <strong>Address:</strong>
                    <p>Cotia</p>

                    <strong>Gender:</strong>
                    <p>Male</p>

                    <button type='button'>
                        <FiEdit size={20} color='251fc5'/>
                    </button>

                    <button type='button'>
                        <FiTrash2 size={20} color='251fc5'/>
                    </button>
                </li>

                <li>
                    <strong>First name:</strong>
                    <p>Bruno</p>

                    <strong>Last name:</strong>
                    <p>Yamada</p>

                    <strong>Address:</strong>
                    <p>Cotia</p>

                    <strong>Gender:</strong>
                    <p>Male</p>

                    <button type='button'>
                        <FiEdit size={20} color='251fc5'/>
                    </button>

                    <button type='button'>
                        <FiTrash2 size={20} color='251fc5'/>
                    </button>
                </li>
            </ul>
        </div>
    )
}