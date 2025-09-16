import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import employeeService from "../services/EmployeeService";

export default function LoginGoogleButton({ style, onLoginResult, onClick }) {
    const navigate = useNavigate();

    useEffect(() => {
        window.google.accounts.id.initialize({
            client_id: "375227342188-ki3mkm14aed5envimjlukj52bpv7mnf2.apps.googleusercontent.com",
            callback: handleCredentialResponse,
        });
        window.google.accounts.id.renderButton(
            document.getElementById("googleSignInDiv"),
            { theme: "outline", size: "large" }
        );
    }, []);

    const handleCredentialResponse = async (response) => {
        const googleToken = response.credential;
        try {
            const res = await employeeService.loginGoogle(googleToken);
            if (onLoginResult) onLoginResult(res);
        } catch (err) {
            if (onLoginResult) onLoginResult(null, err);
        }
    };

    const handleClick = () => {
        if (onClick) onClick();
    }
    return <div id="googleSignInDiv" style={{ ...style }} onClick={handleClick}></div>;
}
