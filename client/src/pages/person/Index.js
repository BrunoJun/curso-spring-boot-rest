import React, {useState, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/Api';

import { Link } from 'react-router-dom';
import logo from '../../assets/logo.svg';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi'

import './styles.css'


export default function Person(){

    const [people, setPeople] = useState([]);

    const navigate = useNavigate();

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    useEffect(() => {

        api.get('api/person/v1', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        }).then(response => {

            setPeople(response.data._embedded.personVOList)
        })
    })

    return (
        <div className='person-container'>
            <header>
                <img src={logo} alt='logo image'/>
                <span>Welcome, <strong>{username.toUpperCase()}</strong></span>
                <Link className='button' to="/person/new">Add new person</Link>
                <button type='button'>
                    <FiPower size={18} color='#251fc5'/>
                </button>
            </header>

            <h1>Registered people</h1>
            <ul>
                {people.map(person => (
                    <li>
                        <strong>First name:</strong>
                        <p>{person.firstName}</p>

                        <strong>Last name:</strong>
                        <p>{person.lastName}</p>

                        <strong>Address:</strong>
                        <p>{person.address}</p>

                        <strong>Gender:</strong>
                        <p>{person.gender}</p>

                        <button type='button'>
                            <FiEdit size={20} color='251fc5'/>
                        </button>

                        <button type='button'>
                            <FiTrash2 size={20} color='251fc5'/>
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    )
}