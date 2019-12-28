# react-native-command-executer

## Getting started

`$ npm install react-native-command-executer --save`

### Mostly automatic installation

`$ react-native link react-native-command-executer`

## Usage
```javascript
import SNCommandExecuter from 'react-native-command-executer';
        // Non root command
        SNCommandExecuter.nonRootExec("echo NOT ROOTED").then((result) => {
                console.log("LOG: "+result.stdout);
            }
        ).catch((error) => {
            console.log("ERROR LOG: "+error);
        });

        // Root command
        SNCommandExecuter.rootExec("echo ROOTED").then((result) => {
                console.log("LOG: "+result.stdout);
            }
        ).catch((error) => {
            console.log("ERROR LOG: "+error);
        });
```
