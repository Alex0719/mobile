import React, { Component } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { LoginForm }  from './LoginForm';

export class Login extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text>Login</Text>
        <View style={styles.formContainer}>
          <LoginForm store={this.props.store}/>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#2c3e50',
  },
});
