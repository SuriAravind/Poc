import 'package:flutter/material.dart';

import './answer.dart';
import './question.dart';

class Quix extends StatelessWidget {
  final List<Map<String, Object>> questions;
  final int questionIndex;
  final Function answerQuestion;

  Quix(
    @required this.answerQuestion,
    @required this.questions,
    @required this.questionIndex,
  );

  @override
  Widget build(BuildContext context) {
    return new Column(
      children: <Widget>[
        Question(
            questions[questionIndex]['questions'].toString().toUpperCase()),
        ...(questions[questionIndex]['answers'] as List<Map<String, Object>>)
            .map((_answer) {
          return Answer(
              () => answerQuestion(_answer['score']), _answer['text']);
        }).toList()
      ],
    );
  }
}
