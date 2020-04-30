import 'package:flutter/material.dart';

import './quiz.dart';
import './result.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _MyAppState();
  }
}

//refer
//"_" public
class _MyAppState extends State<MyApp> {
  var _questionIndex = 0;
  var _totalScore = 0;
  var _questions = const [
    {
      'questions': 'what\'s ur name ?',
      'answers': [
        {'text': 'suriya', 'score': 100},
        {'text': 'shankar', 'score': 0},
        {'text': 'ashok', 'score': 0}
      ]
    },
    {
      'questions': 'what\'s ur nick name ?',
      'answers': [
        {'text': 'pappu', 'score': 100},
        {'text': 'ganesh', 'score': 0},
        {'text': 'kumar', 'score': 0}
      ]
    },
    {
      'questions': 'what\'s ur address ?',
      'answers': [
        {'text': '123', 'score': 100},
        {'text': '456', 'score': 0},
        {'text': '789', 'score': 0}
      ]
    },
  ];

  void _answerQuestion(int score) {
    setState(() {
      _questionIndex = _questionIndex + 1;
    });
    _totalScore += score;
  }

  void _resetQuiz() {
    setState(() {
      _questionIndex = 0;
      _totalScore = 0;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: AppBar(
          title: Text("My App Title"),
        ),
        body: _questionIndex < _questions.length
            ? Quix(_answerQuestion, _questions, _questionIndex)
            : Result(((_totalScore / _questions.length)).toString(),
                _resetQuiz),
      ),
    );
  }
}
