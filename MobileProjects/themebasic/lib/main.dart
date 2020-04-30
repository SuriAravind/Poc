import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import './theme_manager.dart';
import './home.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      //Here we provide our ThemeManager to child widget tree
      builder: (_) => ThemeManager(),

      //Consumer will call builder method each time ThemeManager
      //calls notifyListeners()
      child: Consumer(builder: (context, manager, _) {
        return MaterialApp(
            debugShowCheckedModeBanner: false,
            theme: manager.themeData,
            title: 'Theme app',
            home: Home());
      }),
    );
  }
}
