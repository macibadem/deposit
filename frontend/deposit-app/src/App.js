import React, {useState} from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
  useNavigate
} from 'react-router-dom';
import axios from 'axios';
import './App.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await axios.post(
          'http://localhost:8080/api/v0/auth/login', {
            username,
            password,
          });
      localStorage.setItem('token', response.data.token); // Store token in local storage
      alert('Login successful!');
      navigate('/dashboard'); // Redirect to Dashboard
    } catch (error) {
      alert('Login failed!');
    }
  };

  return (
      <div className="LoginPage" align="center">
        <h2>Login</h2>
        <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
        />
        <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
        />
        <button onClick={handleLogin}>Login</button>
      </div>
  );
};

const Dashboard = () => {
  const [initialCredit, setInitialCredit] = useState('');
  const [userInfo, setUserInfo] = useState(null);

  const handleCreateAccount = async () => {
    try {
      const token = localStorage.getItem('token'); // Retrieve token from local storage
      await axios.post(
          'http://localhost:8080/api/gateway/account/v0/accounts',
          {initialCredit},
          {headers: {Authorization: `Bearer ${token}`}}
      );
      alert('Account created successfully!');
      handleFetchUserInfo();
    } catch (error) {
      alert('Account creation failed!');
    }
  };

  const handleFetchUserInfo = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(
          'http://localhost:8080/api/gateway/query/v0/queries/user-info',
          {headers: {Authorization: `Bearer ${token}`}}
      );
      setUserInfo(response.data);
    } catch (error) {
      alert('Failed to fetch user info!');
    }
  };

  return (
      <div className="Dashboard" align="center">
        <h1>Dashboard</h1>

        <div>
          <h2>Create Account</h2>
          <input
              type="number"
              placeholder="Initial Credit"
              value={initialCredit}
              onChange={(e) => setInitialCredit(e.target.value)}
          />
          <button onClick={handleCreateAccount}>Create Account</button>
        </div>

        <div>
          <h2>User Info</h2>
          <button onClick={handleFetchUserInfo}>Fetch User Info</button>
          {userInfo && (
              <div>
                <table border="solid">
                  <thead>
                  <tr>
                    <th>Name</th>
                    <th>Surname</th>
                    <th>Balance</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>{userInfo.name}</td>
                    <td>{userInfo.surname}</td>
                    <td>{userInfo.balance}</td>
                  </tr>
                  </tbody>
                </table>
                <h4>Transactions:</h4>
                {userInfo.accountInfoList.map((account) => (
                    <div key={account.accountId}>
                      <table border="solid">
                        <tr>
                          <th>Account ID</th>
                          <th>Amount</th>
                          <th>Timestamp</th>
                        </tr>
                        {account.transactions.map((transaction) => (
                            <tr key={transaction.timestamp}>
                              <td>{account.accountId}</td>
                              <td>{transaction.amount}</td>
                              <td>{transaction.timestamp}</td>
                            </tr>
                        ))}
                      </table>
                    </div>
                ))}
              </div>
          )}
        </div>
      </div>
  );
};

const App = () => {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<LoginPage/>}/>
          <Route path="/dashboard" element={<Dashboard/>}/>
        </Routes>
      </Router>
  );
};

export default App;
