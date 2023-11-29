import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/Api';

import logo from '../../assets/logo.svg'
import { Link } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi'

import './styles.css';

export default function NewPerson(){

    const [id, setId] = useState(null);
    const [firstName, setFirstName] = useState(''); 
    const [lastName, setLastName] = useState('');
    const [address, setAddress] = useState('');
    const [gender, setGender] = useState('');
    const [enabled, setEnable] = useState(false);

    const navigate = useNavigate();

    const accessToken = localStorage.getItem('accessToken')

    async function createNewPerson(e){

        e.preventDefault();

        const data = {

            firstName,
            lastName,
            address,
            gender,
            enabled
        }

        try {

            console.log(data)
            
            await api.post('api/person/v1', data,

            {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });

            navigate('/person');
        } catch (error) {
            
            alert('Error while recording person!');
        }
    }

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

                <form onSubmit={createNewPerson}>
                    <input placeholder='First Name' value={firstName} onChange={e => setFirstName(e.target.value)}/>
                    <input placeholder='Last Name' value={lastName} onChange={e => setLastName(e.target.value)}/>
                    <input placeholder='Address' value={address} onChange={e => setAddress(e.target.value)}/>
                    <input placeholder='Gender' value={gender} onChange={e => setGender(e.target.value)}/>
                    <div className='select'>
                        <label for="options">Enabled:</label>
                        <select name='options' value={enabled} onChange={e => setEnable(e.target.value)}>
                            <option>true</option>
                            <option>false</option>
                        </select>
                    </div>
                    <button className='button' type='submit'>Add</button>
                </form>
            </div>
        </div>
    );
}