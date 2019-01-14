import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import {createStore, applyMiddleware, combineReducers} from 'redux';
import thunk from 'redux-thunk';

import { Masina } from './components/Masina';
import { ListaMasini } from './components/ListaMasini';
import { Switch } from './components/Switch';
import { Login } from './components/Login';
import { masiniReducer } from './api/service';

const rootReducer = combineReducers({ masina: masiniReducer});
const store = createStore(rootReducer, applyMiddleware(thunk));

export default class App extends React.Component {

  render() {

    return (

      // <View style={styles.container}>
        // <View style={styles.container}>
        //   <Text style={styles.greeting}>Open up App.js to start working on your app! Friend!</Text>
        // </View>
        // <Masina masina={masina1}></Masina>
      // </View>
      // <Login />
      // <View style={styles.content}>
      //   <Text style={styles.greeting}>Masini care au alimentat</Text>
      //   <ListaMasini store={store} />
      // </View>
      <Switch store={store} />
    );
  }
}
