/**
 * @Author: Aimé
 * @Date:   2022-03-26 17:01:39
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-29 06:32:37
 */
package be.freeaime.drop_block_2048.views;

import javafx.scene.control.Button;

public class ViewBlock extends Button {

    public ViewBlock() {
        setPrefSize(70, 70);
    }

    public void updateType(int type) {
        switch (type) {
            case 0:
                this.setDisable(true);
                getStyleClass().clear();
                getStyleClass().addAll("block-empty");
                setText("");
                break;
            case 1 << 1:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-1");
                setText(formatNumber(type));
                break;
            case 1 << 2:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-2");
                setText(formatNumber(type));
                break;
            case 1 << 3:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-3");
                setText(formatNumber(type));
                break;
            case 1 << 4:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-4");
                setText(formatNumber(type));
                break;
            case 1 << 5:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-5");
                setText(formatNumber(type));
                break;
            case 1 << 6:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-6");
                setText(formatNumber(type));
                break;
            case 1 << 7:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-7");
                setText(formatNumber(type));
                break;
            case 1 << 8:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-8");
                setText(formatNumber(type));
                break;
            case 1 << 9:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-9");
                setText(formatNumber(type));
                break;
            case 1 << 10:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-10");
                setText(formatNumber(type));
                break;
            case 1 << 11:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-11");
                setText(formatNumber(type));
                break;
            default:
                this.setDisable(false);
                getStyleClass().clear();
                getStyleClass().addAll("button", "block-11");
                setText(formatNumber(type));
                break;
        }

    }

    private String formatNumber(int type) {
        if (type > 1000) {
            double k = type / 1000d;
            return String.format("%.2fk", k);
        }
        return Integer.toString(type);
    }

}
