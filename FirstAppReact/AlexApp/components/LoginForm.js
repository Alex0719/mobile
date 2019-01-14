import React, { Component } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert,
  Button, StyleSheet, StatusBar, KeyboardAvoidingView
} from 'react-native';
import { logIn, connectWs, disconnectWs } from "../api/service";

export class LoginForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      nume: '',
      parola: '',
      badCredentials: false,
      issue: ''
    };
  }


  componentWillUnmount() {
    // this.unsubscribe();
    // disconnectWs(this.ws);
  }

  handleLogin (nume,parola) {
    const { store } = this.props;
    store.dispatch(logIn(nume, parola));
    this.unsubscribe = store.subscribe(() => {
      const { logged, issue } = store.getState().masina;
      console.log('logged', logged);
      this.setState({
        logged,
        issue
      })
    });
    //this.ws = connectWs(store);

    if(!this.state.logged){
      this.setState({
        badCredentials: true,
      });
    } else {
      this.setState({
        badCredentials: false
      });
    }

  }

  render() {
    let errorText='';
    if(this.state.badCredentials){
      errorText = 'Nume sau parola incorect/a';
    }
    if(this.state.issue !== ''){
      errorText = 'Server indisponibil';
    }

    return (
      <KeyboardAvoidingView behavior="padding" style={styles.container}>
        <TextInput style = {styles.input}
             autoCapitalize="none"
             onSubmitEditing={() => this.passwordInput.focus()}
             onChangeText={ text => this.setState({nume: text})}
             autoCorrect={false}
             keyboardType='email-address'
             returnKeyType="next"
             placeholder='Nume'
             placeholderTextColor='rgba(225,225,225,0.7)'
        />

        <TextInput style = {styles.input}
            returnKeyType="go"
            ref={(input)=> this.passwordInput = input}
            onChangeText={ text => this.setState({parola: text})}
            placeholder='Parola'
            placeholderTextColor='rgba(225,225,225,0.7)'
            secureTextEntry
        />

        <TouchableOpacity style={styles.buttonContainer}
                   onPress={() => {this.handleLogin(this.state.nume, this.state.parola)}}>
           <Text  style={styles.buttonText}>LOGIN</Text>
        </TouchableOpacity>

        <Text  style={styles.errorText}>{errorText}</Text>

      </KeyboardAvoidingView>
    );
  }
}

const styles = StyleSheet.create({
    container: {
     padding: 20,
     width: 300
    },
    input:{
        height: 40,
        backgroundColor: 'rgba(225,225,225,0.2)',
        marginBottom: 10,
        padding: 10,
        color: '#fff'
    },
    buttonContainer:{
        backgroundColor: '#2980b6',
        paddingVertical: 15
    },
    buttonText:{
        color: '#fff',
        textAlign: 'center',
        fontWeight: '700'
    },

    errorText: {
      color: 'red',
      textAlign: 'center',
      fontWeight: '600'
    }
});
