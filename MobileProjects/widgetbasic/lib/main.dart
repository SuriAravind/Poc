import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:widgetbasic/widgets/usertransaction.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Material App',
      home: MyHomeApp(),
    );
  }
}

class MyHomeApp extends StatelessWidget {
  final titleController = TextEditingController();
  final amountController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: Text('Flutter App'),
      ),
      body: SingleChildScrollView(
        child: Column(
          // mainAxisAlignment: MainAxisAlignment.spaceAround,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            Container(
              width: double.infinity,
              child: Card(
                color: Colors.blue,
                child: Text('TEXT_1'),
              ),
            ),
            UserTranssaction()
          ],
        ),
      ),
    );
  }
}
