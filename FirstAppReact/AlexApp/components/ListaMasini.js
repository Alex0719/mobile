import React, {Component} from 'react';
import { Text, StyleSheet, View } from 'react-native';
import { loadMasini, logIn, connectWs, disconnectWs } from "../api/service";

import { Masina } from './Masina';

export class ListaMasini extends Component {
  constructor(props) {
    super(props);
    this.state = { };
  }

  componentDidMount() {
    //will get from api
    // const masini = [
    //   {
    //     id: 1,
    //     marca: 'VW',
    //     combustibil: 'motorina'
    //   },
    //   {
    //     id: 2,
    //     marca: 'Renault',
    //     combustibil: 'motorina'
    //   },
    //   {
    //     id: 3,
    //     marca: 'Seat',
    //     combustibil: 'benzina 95'
    //   },
    //   {
    //     id: 4,
    //     marca: 'BMW',
    //     combustibil: 'benzina'
    //   },
    //   {
    //     id: 5,
    //     marca: 'Toyota',
    //     combustibil: 'motorina +'
    //   }
    // ];
    const { store } = this.props;
    store.dispatch(loadMasini());
    // store.dispatch(logIn('nume', 'parola'));
    this.unsubscribe = store.subscribe(() => {
      const { masini } = store.getState().masina;
      // const { logged } = store.getState().masina;
      // console.log('logged', logged);
      this.setState({
        masini
      })
    });
    //this.ws = connectWs(store);
  }

  componentWillUnmount() {
    // this.unsubscribe();
    // disconnectWs(this.ws);
  }

  render (){
    const { masini } = this.state;
    return (
      <View style={styles.lista}>
        {masini && masini.map(masina => <Masina key={masina.id} masina={masina} />)}
      </View>
    );
  }
}

const styles = StyleSheet.create ({
  lista: {
    flex: 1
  }
});
