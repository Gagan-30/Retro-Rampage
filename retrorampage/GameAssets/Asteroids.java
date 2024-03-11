//package com.base.game.retrorampage.BAGEL;
//
//public class Asteroids extends Game
//{
//    public Sprite player;
//    public Group rockGroup;
//    public Texture rockTex;
//    public Group bulletGroup;
//    public Texture bulletTex;
//    public Animation explosionAnim;
//    public Sprite shields;
//
//    public void initialize()
//    {
//        setTitle("Asteroids");
//        setWindowSize(800,600);
//
//        Sprite background = new Sprite();
//        Texture bgTex = new Texture("space.png");
//        background.setTexture( bgTex );
//        background.setPosition(400,300);
//        group.add( background );
//
//        player = new Player("player.png");
//        group.add(player);
//
//        rockGroup = new Group();
//        group.add( rockGroup );
//        int rockCount = 8;
//        rockTex = new Texture("asteroid.png");
//        for (int i = 0; i < rockCount; i++)
//        {
//            Sprite rock = new Sprite();
//            rock.setTexture( rockTex );
//            rock.setSize(100,100);
//
//            double angle = 2 * Math.PI * Math.random();
//            double x = player.position.x
//                       + 250 * Math.cos(angle);
//            double y = player.position.y
//                       + 250 * Math.sin(angle);
//            rock.setPosition(x,y);
//
//            rock.setPhysics( new Physics(0, 100, 0) );
//
//            double moveSpeed = 30 * Math.random() + 90;
//            rock.physics.setSpeed(moveSpeed);
//            rock.physics.setMotionAngle(
//                    Math.toDegrees(angle) );
//
//            double rotateSpeed = 2 * Math.random() + 1;
//            rock.addAction(
//                    Action.forever(Action.rotateBy(360, rotateSpeed) )
//            );
//
//            rock.addAction( Action.wrapToScreen(800,600) );
//            rockGroup.add(rock);
//        }
//
//        bulletGroup = new Group();
//        group.add( bulletGroup );
//        bulletTex = new Texture("bullet.png");
//
//        explosionAnim = new Animation(
//                "explosion.png", 6,6, 0.02, false);
//
//        shields = new Sprite();
//        Texture shieldTex = new Texture("shields.png");
//        shields.setTexture( shieldTex );
//        shields.setSize(120,120);
//        group.add(shields);
//    }
//
//    public void update()
//    {
//        shields.alignToSprite(player);
//
//        if ( input.isKeyPressed("LEFT") )
//            player.rotateBy(-3);
//
//        if ( input.isKeyPressed("RIGHT") )
//            player.rotateBy(3);
//
//        if ( input.isKeyPressed("UP") )
//            player.physics.accelerateAtAngle(player.angle);
//
//        if ( input.isKeyJustPressed("SPACE") )
//        {
//            Sprite bullet = new Sprite();
//            bullet.setTexture(bulletTex);
//            bullet.alignToSprite(player);
//            bullet.setPhysics( new Physics(0,400,0) );
//            bullet.physics.setSpeed(400);
//            bullet.physics.setMotionAngle(player.angle);
//            bullet.addAction( Action.wrapToScreen(800,600) );
//            bulletGroup.add(bullet);
//
//            bullet.addAction( Action.delayFadeRemove(1, 0.5) );
//        }
//
//        for (Entity rockE : rockGroup.getList())
//        {
//            Sprite rock = (Sprite)rockE;
//
//            if (shields.overlaps(rock) && shields.opacity > 0)
//            {
//                rock.removeSelf();
//                shields.opacity -= 0.25;
//
//                Sprite explosion = new Sprite();
//                explosion.setAnimation(
//                        explosionAnim.clone() );
//                explosion.alignToSprite(rock);
//                explosion.addAction( Action.animateThenRemove() );
//
//                group.add( explosion );
//            }
//
//            // game over
//            if (rock.overlaps(player))
//            {
//                // player.removeSelf();
//                // game over message appears
//            }
//
//            for (Entity bulletE : bulletGroup.getList())
//            {
//                Sprite bullet = (Sprite)bulletE;
//                if (rock.overlaps(bullet))
//                {
//                    rockGroup.remove(rock);
//                    bulletGroup.remove(bullet);
//                    Sprite explosion = new Sprite();
//                    explosion.setAnimation(
//                            explosionAnim.clone() );
//                    explosion.alignToSprite(rock);
//
//                    explosion.addAction( Action.animateThenRemove() );
//
//                    group.add( explosion );
//
//                    // if rock is large (100x100),
//                    //  split into two smaller rocks
//                    if (rock.width == 100)
//                    {
//                        for (int i = 0; i < 2; i++)
//                        {
//                            Sprite rockSmall = new Sprite();
//                            rockSmall.setTexture(rockTex);
//                            rockSmall.setSize(50, 50);
//                            rockSmall.alignToSprite(rock);
//                            rockSmall.addAction(Action.wrapToScreen(800,600));
//                            rockSmall.setPhysics(new Physics(0, 300, 0));
//                            rockSmall.physics.setSpeed(
//                                     2 * rock.physics.getSpeed());
//                            rockSmall.physics.setMotionAngle(
//                                    rock.physics.getMotionAngle() + 90*Math.random() - 45);
//                            rockGroup.add(rockSmall);
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
