//////package com.base.game.retrorampage.BAGEL;
//////
//////public class TestGame extends Game {
//////    Sprite ball;
//////
//////    public void initialize() {
//////        setTitle("Test BAGEL.Game");
//////        setWindowSize(800, 600);
//////        System.out.println("Hello, world!");
//////
//////        ball = new Sprite();
//////        ball.setPosition(10, 10);
//////        ball.setTexture(Texture.load("images/basketball.png"));
//////        ball.setSize(50, 50);
//////
//////        group.add(ball);
//////    }
//////
//////    public void update() {
//////        if (input.isKeyPressed("RIGHT"))
//////            ball.position.addValues(2, 0);
//////
//////        if (input.isKeyPressed("LEFT"))
//////            ball.position.addValues(-2, 0);
//////
//////        if (input.isKeyPressed("UP"))
//////            ball.position.addValues(0, -2);
//////
//////        if (input.isKeyPressed("DOWN"))
//////            ball.position.addValues(0, 2);
//////
//////        if (input.isKeyJustPressed("Z"))
//////            ball.position.setValues(0, 0);
//////    }
//////}
////
////
////
////
////for (Rect roomRectangle : roomManager.getRoomRectangles()) {
////        resolveCollision(roomRectangle);
////        }
////        }
////
////private void resolveCollision(Rect obstacle) {
////        if (player != null) {
////        // Get the start and end coordinates for the width and height of the obstacle
////        double obstacleStartX = obstacle.getX();
////        double obstacleEndX = obstacle.getX() + obstacle.getWidth();
////        double obstacleStartY = obstacle.getY();
////        double obstacleEndY = obstacle.getY() + obstacle.getHeight();
////
////        // Calculate the potential new position after collision resolution
////        double newX = player.position.getX();
////        double newY = player.position.getY();
////
////        // Check if the new position is within the bounds of the room or hallway
////        boolean withinRoomBounds = newX >= obstacleStartX && newX + player.getWidth() <= obstacleEndX &&
////        newY >= obstacleStartY && newY + player.getHeight() <= obstacleEndY;
////
////        // Check if the new position is within the bounds of any hallway
////        boolean withinHallwayBounds = false;
////        Rect[] hallwayBoundsList = corridorManager.getHallwayBounds();
////        if (hallwayBoundsList != null) {
////        for (Rect hallwayBounds : hallwayBoundsList) {
////        if (newX >= hallwayBounds.getX()
////        && newX + player.getWidth() <= hallwayBounds.getX() + hallwayBounds.getWidth() &&
////        newY >= hallwayBounds.getY()
////        && newY + player.getHeight() <= hallwayBounds.getY() + hallwayBounds.getHeight()) {
////        withinHallwayBounds = true;
////        break; // No need to check further if within bounds of any hallway
////        }
////        }
////        }
////
////        // Adjust the position only if it's not within the bounds of the room
////        if (!withinRoomBounds) {
////        // If within the bounds of the hallway, allow movement; otherwise, prevent it
////        if (withinHallwayBounds) {
////        // Update the position
////        player.setPosition(newX, newY);
////        }
////        }
////        }
//
//
//
//private void drawPlayerInCenter(Cell center) {
//        double squareSize = 50; // Adjust dimensions as needed
//        this.square = new Rect(squareSize, squareSize); // Use class field
//        this.square.setX(center.getCenterX() - square.getWidth() / 2);
//        this.square.setY(center.getCenterY() - square.getHeight() / 2);
//        this.square.setFill(Color.TRANSPARENT); // Set the fill color as needed
//        root.getChildren().add(this.square);
//
//        Image image = new Image("player.png");
//        this.playerImageView = new ImageView(image); // Use class field
//        this.playerImageView.setFitWidth(squareSize);
//        this.playerImageView.setFitHeight(squareSize);
//        this.playerImageView.setX(center.getCenterX() - squareSize / 2);
//        this.playerImageView.setY(center.getCenterY() - squareSize / 2);
//        root.getChildren().add(this.playerImageView);
//        updatePlayerPosition();
//        }
//
//
//        void updatePlayerPosition() {
//        // Update input state
//        input.update();
//
//        // Move the square based on arrow key input
//        double speed = 5.0;
//        String moveUpKey1 = config.getKeybind("MoveUp1");
//        String moveUpKey2 = config.getKeybind("MoveUp2");
//        String moveDownKey1 = config.getKeybind("MoveDown1");
//        String moveDownKey2 = config.getKeybind("MoveDown2");
//        String moveLeftKey1 = config.getKeybind("MoveLeft1");
//        String moveLeftKey2 = config.getKeybind("MoveLeft2");
//        String moveRightKey1 = config.getKeybind("MoveRight1");
//        String moveRightKey2 = config.getKeybind("MoveRight2");
//
//        if (input.isKeyPressed(moveUpKey1)) {
//        square.setY(square.getY() - speed);
//        playerImageView.setY(playerImageView.getY() - speed);
//        }
//        if (input.isKeyPressed(moveUpKey2)) {
//        square.setY(square.getY() - speed);
//        playerImageView.setY(playerImageView.getY() - speed);
//        }
//        if (input.isKeyPressed(moveDownKey1)) {
//        square.setY(square.getY() + speed);
//        playerImageView.setY(playerImageView.getY() + speed);
//        }
//        if (input.isKeyPressed(moveDownKey2)) {
//        square.setY(square.getY() + speed);
//        playerImageView.setY(playerImageView.getY() + speed);
//        }
//        if (input.isKeyPressed(moveLeftKey2)) {
//        square.setX(square.getX() - speed);
//        playerImageView.setX(playerImageView.getX() - speed);
//        }
//        if (input.isKeyPressed(moveLeftKey1)) {
//        square.setX(square.getX() - speed);
//        playerImageView.setX(playerImageView.getX() - speed);
//        }
//        if (input.isKeyPressed(moveRightKey1)) {
//        square.setX(square.getX() + speed);
//        playerImageView.setX(playerImageView.getX() + speed);
//        }
//        if (input.isKeyPressed(moveRightKey2)) {
//        square.setX(square.getX() + speed);
//        playerImageView.setX(playerImageView.getX() + speed);
//        }
//
//        // Update the player's looking direction based on mouse movement
//        double mouseX = input.getMouseX();
//        double mouseY = input.getMouseY();
//
//        double playerCenterX = playerImageView.getX() + playerImageView.getFitWidth() / 2;
//        double playerCenterY = playerImageView.getY() + playerImageView.getFitHeight() / 2;
//
//        double angleToMouse = Math.toDegrees(Math.atan2(mouseY - playerCenterY, mouseX - playerCenterX));
//
//        playerImageView.setRotate(angleToMouse);
//        }