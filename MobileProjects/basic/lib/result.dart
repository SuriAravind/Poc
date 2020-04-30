import 'package:flutter/material.dart';

class Result extends StatelessWidget {
  final String score;
  final Function resetHandler;

  Result(this.score, this.resetHandler);

  @override
  Widget build(BuildContext context) {
    return Center(
        child: new Column(
      mainAxisAlignment: MainAxisAlignment.center,
      // Center Column contents vertically
      crossAxisAlignment: CrossAxisAlignment.center,
      // Center Column contents horizontally
      children: <Widget>[
        Text(
          'you did it ',
          style: new TextStyle(fontSize: 36, fontWeight: FontWeight.bold),
        ),
        new Row(
          mainAxisAlignment: MainAxisAlignment.center,
          // Center Row contents horizontally
          crossAxisAlignment: CrossAxisAlignment.center,
          // Center Row contents vertically
          children: <Widget>[
            Text(
              'Score : ',
              textAlign: TextAlign.center,
              style: new TextStyle(fontSize: 36, fontWeight: FontWeight.bold),
            ),
            Text(
              (score.substring(0, 5) + '%'),
              textAlign: TextAlign.center,
              style: new TextStyle(fontSize: 36, fontWeight: FontWeight.bold),
            ),
          ],
        ),
        FlatButton(
          child: Text('Reset Quiz'),
          textColor: Colors.blue,
          onPressed: resetHandler,
        )
      ],
    ));
  }
}
