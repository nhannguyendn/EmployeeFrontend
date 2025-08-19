import React, { Component } from 'react';
import PropTypes from 'prop-types';

class HeaderComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {

        }
    }

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark px-3 py-2">
                        <div><a href='https:google.com.vn' className='text-white'>Search</a></div>
                    </nav>
                </header>
            </div>
        );
    }
}

export default HeaderComponent;