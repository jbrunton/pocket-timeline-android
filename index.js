import 'react-native-gesture-handler';
import * as React from 'react';
import { AppRegistry, Button, Text } from 'react-native'
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import {TimelineScreen} from 'PocketTimelineReact';

const Stack = createStackNavigator();

const HomeScreen = ({ navigation }) => {
  return (
    <Button
      title="Show Timeline"
      onPress={() =>
        navigation.navigate('TimelineScreen', { timelineId: "1" })
      }
    />
  );
};

export default TimelineApp = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          options={{ title: 'Welcome' }}
        />
        <Stack.Screen name="TimelineScreen" component={TimelineScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

AppRegistry.registerComponent(
  'MyReactNativeApp',
  () => TimelineApp
);
