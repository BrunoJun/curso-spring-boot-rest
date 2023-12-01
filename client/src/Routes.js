import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Login from './pages/login/Index';
import Person from './pages/person/Index';
import NewPerson from './pages/newPerson/Index';

export default function AppRoutes(){

    return (

        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Login/>}/>
                <Route path='/person' element={<Person/>}/>
                <Route path='/person/new/:personId' element={<NewPerson/>}/>
            </Routes>
        </BrowserRouter>
    );
}