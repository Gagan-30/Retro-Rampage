package com.base.game.retrorampage.GameAssets;

import java.util.ArrayList;
import java.util.Arrays;

public class Action {
    public Function function;
    public double totalTime;
    public Action() {
        totalTime = 0;
    }

    public Action(Function f) {
        function = f;
        totalTime = 0;
    }

    public static Action moveBy(
            double deltaX, double deltaY, double duration) {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    target.moveBy(deltaX / duration * dt,
                            deltaY / duration * dt);
                    return (tt >= duration);
                }
        );
    }

    public static Action rotateBy(double deltaA, double duration) {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    target.rotateBy(deltaA / duration * dt);
                    return (tt >= duration);
                }
        );
    }

    public static Action fadeOut(double duration) {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    target.opacity -= (1 / duration * dt);
                    if (target.opacity < 0)
                        target.opacity = 0;
                    return (tt >= duration);
                }
        );
    }

    // static functions to create BAGEL.Action objects

    public static Action removeSelf() {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    return true;
                }
        );
    }

    public static Action isAnimationFinished() {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    return target.animation.isFinished();
                }
        );
    }

    public static Action repeat(Action action, int totalTimes) {
        return new Action() {
            int finishedTimes = 0;

            @Override
            public boolean apply(Sprite target, double dt) {
                boolean finished = action.apply(target, dt);
                if (finished) {
                    finishedTimes += 1;
                    action.reset();
                }
                return (finishedTimes == totalTimes);
            }
        };
    }

    public static Action forever(Action action) {
        return new Action() {
            @Override
            public boolean apply(Sprite target, double dt) {
                boolean finished = action.apply(target, dt);

                if (finished)
                    action.reset();

                return false;
            }
        };
    }

    public static Action sequence(Action... actions) {
        return new Action() {
            final ArrayList<Action> actionList =
                    new ArrayList<Action>(Arrays.asList(actions));
            int currentIndex;

            @Override
            public boolean apply(Sprite target, double dt) {
                Action currentAction = actionList.get(currentIndex);
                boolean finished = currentAction.apply(target, dt);
                if (finished) {
                    currentIndex += 1;
                }
                return (currentIndex == actionList.size());
            }

            @Override
            public void reset() {
                for (Action action : actionList)
                    action.reset();

                currentIndex = 0;
            }
        };
    }

    // meta-actions

    public static Action delay(double duration) {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    return (tt >= duration);
                }
        );
    }

    // screen-based actions
    public static Action boundToScreen(
            int screenWidth, int screenHeight) {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    target.boundToScreen(screenWidth, screenHeight);
                    return false;
                }
        );
    }

    public static Action wrapToScreen(
            int screenWidth, int screenHeight) {
        return new Action(
                (Sprite target, double dt, double tt) ->
                {
                    target.wrapToScreen(screenWidth, screenHeight);
                    return false;
                }
        );
    }

    // compound actions
    public static Action delayFadeRemove(
            double delayTime, double fadeTime) {
        return Action.sequence(
                Action.delay(delayTime),
                Action.fadeOut(fadeTime),
                Action.removeSelf()
        );
    }

    public static Action animateThenRemove() {
        return Action.sequence(
                Action.isAnimationFinished(),
                Action.removeSelf()
        );
    }

    public boolean apply(Sprite target, double deltaTime) {
        totalTime += deltaTime;
        return function.run(target, deltaTime, totalTime);
    }

    public void reset() {
        totalTime = 0;
    }

    public interface Function {
        boolean run(Sprite target, double deltaTime, double totalTime);
    }
}
