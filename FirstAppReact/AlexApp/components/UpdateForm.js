import React, { Component } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert, Button,
StyleSheet, StatusBar, KeyboardAvoidingView } from 'react-native';
import { updateMasina, connectWs, disconnectWs } from "../api/service";


export class UpdateForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      marca: '',
      combustibil: '',
      id: '',
      issue:''
    };
  }

  componentWillUnmount() {
    // this.unsubscribe();
    // disconnectWs(this.ws);
  }

  handleUpdate(id, marca, combustibil) {
    console.log('will save', id, marca, combustibil);
    const { store } = this.props;
    store.dispatch(updateMasina(id, marca, combustibil));
    // this.unsubscribe = store.subscribe(() => {
    //
    // });
    // this.ws = connectWs(store);
  }

  render() {
    let errorText='';
    if(this.state.issue !== ''){
      errorText = 'Server indisponibil';
    }

    return (
      <KeyboardAvoidingView behavior="padding" style={styles.container}>
        <TextInput style={styles.input}
          onChangeText={text => this.setState({id: text})}
          placeholder='Id'
        />

        <TextInput style = {styles.input}
             autoCapitalize="none"
             onSubmitEditing={() => this.passwordInput.focus()}
             onChangeText={ text => this.setState({marca: text})}
             autoCorrect={false}
             keyboardType='email-address'
             returnKeyType="next"
             placeholder='Marca'
             placeholderTextColor='rgba(125,125,125,0.7)'
        />

        <TextInput style = {styles.input}
            returnKeyType="go"
            ref={(input)=> this.passwordInput = input}
            onChangeText={ text => this.setState({combustibil: text})}
            placeholder='Combustibil'
            placeholderTextColor='rgba(125,125,125,0.7)'
        />

        <TouchableOpacity style={styles.buttonContainer}
                   onPress={() => {this.handleUpdate(this.state.id,this.state.marca, this.state.combustibil)}}>
           <Text  style={styles.buttonText}>SALVEAZA</Text>
        </TouchableOpacity>

        <Text  style={styles.errorText}>{errorText}</Text>

      </KeyboardAvoidingView>
    );
  }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
     marginTop: 0,
     padding: 20,
     width: 300
    },
    input:{
        height: 40,
        backgroundColor: 'rgba(225,225,225,0.2)',
        marginBottom: 10,
        padding: 10,
        color: '#333'
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
