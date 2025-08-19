import React, { Component } from 'react';

class FooterComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {

        }
    }

    render() {
        return (
            <footer className="bg-dark">
                <div className="container py-2 text-white text-center">
                    All right Reserved
                </div>
            </footer>
        );
    }
}

export default FooterComponent;