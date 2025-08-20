import { useNavigate, useParams } from "react-router-dom";

export function withNavigation(Component) {
  return function Wrapper(props) {
    const navigate = useNavigate();
    const params = useParams();
    return <Component {...props} navigate={navigate} params={params} />;
  };
}
