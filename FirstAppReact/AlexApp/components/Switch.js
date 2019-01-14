import React, { Component } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Login }  from './Login';
import { ListaMasini }  from './ListaMasini';
import { UpdateForm } from './UpdateForm';
import { connectWs, disconnectWs } from "../api/service";


export class Switch extends Component {
  constructor(props) {
    super(props);
    this.state = {
      logged: false
    }
  }

  componentDidMount() {
    const { store } = this.props;
    this.unsubscribe = store.subscribe(() => {
      const { logged } = store.getState().masina;
      console.log('logged switch', logged);
      this.setState({
        logged
      })
    });
    this.ws = connectWs(store);
  }

  componentWillUnmount(){
    this.unsubscribe();
    disconnectWs(this.ws);
  }

  render() {
    if(!this.state.logged){
      return <Login store={this.props.store} />
    } else {
      return (
        <View style={styles.content}>
          <Text style={styles.greeting}>Masini care au alimentat</Text>
          <ListaMasini store={this.props.store} />
          <UpdateForm store={this.props.store} />
        </View>
      );
    }
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#2c3e50',
  },

  greeting: {
    color: '#3f5',
    fontFamily: 'serif',
    fontSize: 25,
    fontWeight: 'normal',
    fontStyle: 'italic',
    letterSpacing: 0.1,
    lineHeight: 25,
    paddingTop: 2,
    textAlign: 'center',
    backgroundColor: '#64b'
  },

  container: {
    flex: 1,
    backgroundColor: '#67b',
    alignItems: 'center',
    justifyContent: 'center',
  },

  content: {
    marginTop: 24,
    flex: 1
  }
});
