import 'react-native-gesture-handler';
import * as React from 'react';
import { AppRegistry, Button, Text } from 'react-native'
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import {TimelineScreen} from 'PocketTimelineReact';

const Stack = createStackNavigator();

export default TimelineApp = (props) => {
  console.log("props: " + JSON.stringify(props))
  const HomeScreen = ({ navigation }) => {
    return (
      <Button
        title="Show Timeline"
        onPress={() =>
          navigation.navigate('TimelineScreen', { timelineId: props.timeline_id })
        }
      />
    );
  };
  
  return (
    <NavigationContainer>
      <Stack.Navigator props={props}>
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          props={props}
          options={{ title: props.timeline_title }}
        />
        <Stack.Screen name="TimelineScreen" component={TimelineScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

function Welcome(props) {
  return <Text>Hello, {props.AUDIENCE}!</Text>;
}

AppRegistry.registerComponent('ReactTest', () => TimelineApp);


//AppRegistry.registerComponent('ReactTest', () => Welcome)
