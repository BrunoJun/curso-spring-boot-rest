import React, {useState, useEffect} from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import api from '../../services/Api';

import logo from '../../assets/logo.svg'
import { FiArrowLeft } from 'react-icons/fi'

import './styles.css';

export default function NewPerson(){

    const [id, setId] = useState(null);
    const [firstName, setFirstName] = useState(''); 
    const [lastName, setLastName] = useState('');
    const [address, setAddress] = useState('');
    const [gender, setGender] = useState('');
    const [enabled, setEnable] = useState(false);

    const {personId} = useParams();

    const navigate = useNavigate();

    const accessToken = localStorage.getItem('accessToken')

    async function loadPerson(){

        try {
            
            const response = await api.get(`api/person/v1/${personId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });

            setId(response.data.id);
            setFirstName(response.data.firstName);
            setLastName(response.data.lastName);
            setGender(response.data.gender);
            setAddress(response.data.address);
            setEnable(response.data.enabled);
        } catch (error) {
            
            alert('Error while recoverin person!');
            navigate('/person');
        }
    }

    useEffect(() => {

        if (personId === '0') {
            
            return;
        } else {

            loadPerson();
        }

    }, [personId])


    async function saveOrUpdate(e){

        e.preventDefault();

        const data = {

            firstName,
            lastName,
            address,
            gender,
            enabled
        }

        try {

            if (personId === '0'){

                await api.post('api/person/v1', data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });   
            } else {

                data.id = personId;

                await api.put('api/person/v1', data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
            }       

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
                    <h1>{personId === '0' ? "Add " : "Update "} new person</h1>
                    <p>Enter the person information and click on {personId === '0' ? "'Add'" : "'Update'"}</p>
                    <Link className='back-link' to='/person'>
                        <FiArrowLeft size={16} color='#251fc5'/>
                        Home
                    </Link>
                </section>

                <form onSubmit={saveOrUpdate}>
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
                    <button className='button' type='submit'>{personId === '0' ? 'Add' : 'Update'}</button>
                </form>
            </div>
        </div>
    );
}