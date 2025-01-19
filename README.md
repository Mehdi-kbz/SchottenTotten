# Schotten-Totten Project


![Diagram](https://github.com/user-attachments/assets/ffc9f79a-fa48-47e3-9fad-efb68d6e1538)


## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Project Setup](#project-setup)
- [Usage](#usage)
- [Game Variants](#game-variants)
- [Classes and Responsibilities](#classes-and-responsibilities)
- [UML Diagram](#uml-diagram)
- [Challenges](#challenges)
- [Conclusion](#conclusion)
- [Contributors](#contributors)

---

## Overview

The **Schotten-Totten** project is a Java-based implementation of the popular board game. It is designed with a focus on object-oriented principles and modular architecture, enabling multiple gameplay modes and features such as AI opponents.

---

## Features

- **Core Gameplay**: Implements standard rules of Schotten-Totten.
- **Game Variants**:
  - Standard mode.
  - Tactic mode with special tactical cards.
  - Experts mode for advanced rules.
- **Tactical Cards**: Adds unique effects, enhancing strategy.
- **AI Opponent**: Offers challenging gameplay with an intelligent computer player.
- **Error Handling**: Robust system for handling invalid moves and states.
- **Local Play**: Designed for two players or player vs. AI.

---

## Architecture

The project is divided into key components:

- **`Carte`**: Represents game cards, including their color and value.
- **`Muraille`**: Manages the game zones (walls) where players place cards.
- **`Joueur`**: Represents players and their actions.
- **`Jeu`**: The main game engine that coordinates rules and turns.
- **`TacticalCard`**: Extends `Carte` to include special tactical effects.
- **`IA`**: Implements logic for AI decision-making.
- **`ScoreManager`**: Tracks scores and determines the winner.

---

## Project Setup

### Requirements

- **Java Development Kit (JDK)**: Version 11 or higher.
- **IDE**: Eclipse, IntelliJ IDEA, or any preferred Java IDE.

### Installation and Configuration

1. Open the project in your IDE.
2. Compile and run the `console.view` class.
3. Select your preferred game mode (`Standard`, `Tactic`, or `Experts`).
4. Follow the on-screen instructions to start playing.

---

## Usage

### Starting the Game

1. Launch the `console.view` class in your IDE.
2. Select the mode:
   - **Standard**: Classic gameplay.
   - **Tactic**: Adds tactical cards for strategic options.
   - **Experts**: Advanced rules for experienced players.
3. Configure player settings (Human vs. Human or Human vs. AI).

### Gameplay

- Players place cards on walls to form the strongest combinations.
- Tactical cards (if enabled) allow for special moves.

---

## Game Variants

1. **Standard**: Classic Schotten-Totten gameplay.
2. **Tactic**: Adds cards like:
   - `Traitor`: Allows stealing a card.
   - `Strategist`: Enables special moves.
   - `Banshee`: Adds a disruptive effect.
3. **Experts**: Includes enhanced rules for claiming walls.

---

## Classes and Responsibilities

- **`Carte`**: Represents basic cards with attributes like color and value.
- **`Muraille`**: Handles card placements and victory conditions for walls.
- **`Joueur`**: Manages player actions, including playing and drawing cards.
- **`Jeu`**: The game engine, controlling turns and rules.
- **`TacticalCard`**: A special type of card with unique effects.
- **`IA`**: Implements AI logic for gameplay.
- **`ScoreManager`**: Tracks and manages scores across games.

---

## UML Diagram
![Diagram](https://github.com/user-attachments/assets/ffc9f79a-fa48-47e3-9fad-efb68d6e1538)


---

## Challenges

- **Game Variants**: Implementing diverse gameplay modes.
- **AI Development**: Creating a challenging and strategic AI opponent.
- **Error Handling**: Ensuring smooth gameplay even with invalid moves.

---

## Conclusion

The **Schotten-Totten** project demonstrates advanced object-oriented programming and modular design principles. It offers a complete digital adaptation of the board game with multiple gameplay modes and challenging AI.

---

## Contributors

- **Mehdi Khabouze**: Developer
- **Thomas Meyer**: Developer
- **Samy Maach**: Developer
- **Encadrant**: Sabri Khamari

--- 

Let me know if you want further refinements or additional content!
