import { httpApiUrl, wsApiUrl, headers } from './api';
import { AsyncStorage } from "react-native";

export const loadMasini = () => (dispatch) => {
  console.log('in loadMasini');
  const token = AsyncStorage.getItem('token').then(tok =>{
    dispatch({ type: 'LOAD_STARTED' });
    fetch(`${httpApiUrl}/api/masini`, {
      headers: {
        'Authorization': `Bearer ${tok}`
      }
    })
      .then(response => response.json()) //responseJson.notes
      .then(responseJson =>{

        AsyncStorage.setItem('masini', JSON.stringify(responseJson));
        console.log('in service1', responseJson);
          return dispatch({ type: 'LOAD_SUCCEEDED', payload: responseJson })
      })
      .catch(error => {
        AsyncStorage.getItem('masini').then(masini => {
          console.log('masini storage', masini);
          if(masini){
            const parsedMasini = JSON.parse(masini);
            return dispatch({type: 'LOAD_SUCCEEDED', payload: parsedMasini});
          }
        });
        return dispatch({ type: 'LOAD_FAILED', error });
      });
  });

};

export const logIn = (nume, parola) => dispatch => {
  console.log('in LogIn');
  const credentials = JSON.stringify({nume: nume, parola: parola});
  dispatch({ type: 'LOGIN_REQUEST'});
  fetch(`${httpApiUrl}/api/login`, {
    method: 'post',
    headers: headers,
    body: credentials
  }).then(response => response.json())
    .then(responseJson => {
      AsyncStorage.setItem('credentials', credentials);
      // console.log('toktok', responseJson.token);
      AsyncStorage.setItem('token', responseJson.token);
      return dispatch({ type: 'LOGIN_SUCCEEDED', payload: responseJson });
    })
    .catch(error => {
      AsyncStorage.getItem('credentials').then(credentials => {
        const parsedCredentials = JSON.parse(credentials);
        if(parsedCredentials){
          if( parsedCredentials.nume === nume && parsedCredentials.parola == parola){
              return dispatch({type: 'LOGIN_SUCCEEDED', payload: {found: true}});
          }
        }
      });
      return dispatch({ type: 'LOGIN_FAILED', error });
  });
}

export const updateMasina = (id, marca, combustibil) => dispatch => {
  console.log('in update');
  const masina = JSON.stringify({id:id, marca:marca, combustibil:combustibil});
  dispatch({type: 'UPDATE_REQUEST'});
  fetch(`${httpApiUrl}/api/masini`, {
    method: 'put',
    headers: headers,
    body: masina
  }).then(response => response.json())
    .then(responseJson => {
      AsyncStorage.setItem('masini', JSON.stringify(responseJson));
      console.log('in service', responseJson);
      return dispatch({type: 'UPDATE_SUCCEEDED', payload: responseJson});
    })
    .catch(error => {
      AsyncStorage.getItem('masini').then(masini => {
        console.log('masini storage', masini);
        const parsedMasini = JSON.parse(masini);
        if(parsedMasini){
          const updatedMasini = parsedMasini.masini.map(masina => {
            if(masina.id == id){
              return {id: id, marca: marca, combustibil: combustibil};
            } else {
              return masina;
            }
          });
          AsyncStorage.setItem('masini', JSON.stringify({ masini: updatedMasini }));
          return dispatch({type: 'UPDATE_SUCCEEDED', payload: { masini: updatedMasini }});
        }
      });
      return dispatch({ type: 'UPDATE_FAILED', error });
    });
}

export const masiniReducer = (state = {  masini: [], logged: false, issue:'' }, action) => {
  switch (action.type) {
    case 'LOAD_STARTED':
      return { ...state,  masini: [] };
    case 'LOAD_SUCCEEDED':
    case 'UPDATE_SUCCEEDED':
    // console.log('succeeded', action.payload);
      return { ...state, masini: action.payload.masini };
    case 'LOAD_FAILED':
    case 'UPDATE_FAILED':
      return { ...state, issue: action.error };
    case 'LOGIN_REQUEST':
      return { ...state,  logged: false, issue: '' };
    case 'LOGIN_SUCCEEDED':
      return {...state, logged: action.payload.found };
    case 'LOGIN_FAILED':
      return {...state, logged: false, issue: action.error.message };
    case 'CAR_ADDED':
      return { ...state, masini: action.payload };
      // return { ...state, masini: (state.masini || []).concat([action.payload.masini]) };
    default:
      return state;
  }
};

export const connectWs = (store) => {
  console.log('in connectWs');
  const ws = new WebSocket(wsApiUrl);
  ws.onopen = () => {
    console.log('ws open');
    AsyncStorage.getItem('masini').then(masini => {
      const options = {
        headers: headers,
        credentials: 'include',
        method: 'PUT',
        body: masini,
      };
      fetch(`${httpApiUrl}/synchronize`, options);
    });
  };

  ws.onmessage = e => {
    console.log('got a message from socket', JSON.parse(e.data));
    store.dispatch({
      type: 'CAR_ADDED', payload: JSON.parse(e.data).masini
    });
  }
  ws.onerror = e => {};
  ws.onclose = e => {};
  return ws;
};

export const disconnectWs = (ws) => {
  ws.close();
};
