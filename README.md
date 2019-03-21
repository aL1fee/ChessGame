Chess Game
==============

A traditional Chess game with a GUI. Has the following features: ability to play vs. a human and AI for either side, setting AI difficulty, saving and loading the game (serialization), undoing a move, rotating the board, printing the sequence of moves made in the game in its traditonal notation (ex. b4xc5).

/* Still need to implement:
    1. King castling, en passant moves.
    2. King castling, en passant moves, mate printing notation.

   Areas to improve:
    1. Do a bitboard representation of the chess board for
    minMax to do considerably less copying of the Board class.
    2. Add AI strategies: use a number of good chess openings,
    improve piece development, add a center occupation priority,
    add strategies to hunt/save the queen, implement a priority
    to give a checkmate rather than gain material advantage,
    divide the usage of different strategies based on the timeline
    of the game (early-game, middle-game, end-game).
    3. Add multiple AI difficulty levels incorporating different
    AI strategies.
 */
 
![Application screen-shot](https://user-images.githubusercontent.com/47246379/54778341-9d405d80-4bd1-11e9-8a0c-36718dca2ad6.png)
