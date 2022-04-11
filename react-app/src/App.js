import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'css/theme-common.css';
import 'css/theme-dark.css';
import Home from "Home";
import SearchPage from 'search/pages/SearchPage';
import StatisticsPage from 'statistics/pages/StatisticsPage';

function App() {
  return (
    <Router>
      <Switch>
        <Route exact path='/' component={Home} />
        <Route exact path='/search' component={SearchPage} />
        <Route exact path='/statistics' component={StatisticsPage} />
      </Switch>
    </Router>
  );
}

export default App;
