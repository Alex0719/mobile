import React, {Component} from 'react';
import { Text, StyleSheet, View } from 'react-native';

export class Masina extends Component {
  constructor(props){
    super(props);
  }

  render() {
    const {id, marca, combustibil} = this.props.masina;
    return (
      <View style={styles.viewMasina}>
        <Text style={styles.textMasina}>{id} {marca} a pus {combustibil}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  textMasina: {
    color: '#2b3',
    fontFamily: 'serif',
    fontSize: 20,
    fontWeight: 'normal',
    letterSpacing: 0.1,
    lineHeight: 20,
    textAlign: 'left',
  },

  viewMasina: {
    backgroundColor: '#67b',
    height: 25,
    paddingTop: 5,
    paddingLeft: 20
  }
});
