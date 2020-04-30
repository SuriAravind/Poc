import 'package:flutter/material.dart';

class Question extends StatelessWidget {
  String questionTest;

  Question(this.questionTest);

  @override
  Widget build(BuildContext context) {
    return new Container(
        width: double.infinity,
        margin: EdgeInsets.all(20),
        child: Text(
          questionTest,
          style: new TextStyle(fontSize: 28),
          textAlign: TextAlign.center,
        ));
  }
}
