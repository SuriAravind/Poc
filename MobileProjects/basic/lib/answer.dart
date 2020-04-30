import 'package:flutter/material.dart';

class Answer extends StatelessWidget {
  final Function selectHandler;
  String answertext;

  Answer(this.selectHandler, this.answertext);

  @override
  Widget build(BuildContext context) {
    return new Container(
      width: double.infinity,
      margin: EdgeInsets.all(20),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.black,
        child: Text(answertext),
        onPressed: selectHandler,
      ),
    );
  }
}
