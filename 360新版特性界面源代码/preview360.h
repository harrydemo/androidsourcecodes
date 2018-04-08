#ifndef PREVIEW360_H
#define PREVIEW360_H

#include <QFrame>
#include <QtCore>
#include <QtGui>

#include "clabel.h"
#include "common.h"

class Preview360 : public QFrame
{
    Q_OBJECT
public:
    explicit Preview360(QWidget *parent = 0);
    ~Preview360();

    enum EButtonMouseState
    {
        EButtonMouseDefault = 0,
        EButtonMouseEnter,
        EButtonMousePress,
        EButtonMouseNone
    };

protected:
    void paintEvent(QPaintEvent *);
    void mousePressEvent(QMouseEvent *);
    void mouseReleaseEvent(QMouseEvent *);
    void mouseMoveEvent(QMouseEvent *);
    void keyPressEvent(QKeyEvent *);
    bool eventFilter(QObject *, QEvent *);
    
signals:
    
private slots:
    void slotLabelButtonPress(CLabel *);
    void slotChangeCurrentPage(CLabel *);
    
private:
    //functions
    void initSetupUi();
    void initVariable();
    void createFrame();
    void createWidget();
    void createEventFilter();
    void setButtonIcon(QToolButton *btn, EButtonMouseState state);
    void changeCurrentButton();
    void setLabelMove(bool);
    bool getLabelMove();
    void moveCurrentPage(bool);
    void freeMemory();

    //members
    QPoint m_mouseSrcPos;
    QPoint m_mouseDstPos;
    int m_currentFgXpos;
    bool m_mousePressFlag;
    bool m_mouseMoveWindowFlag;
    bool m_labelMoveFlag;

    QLabel *m_pLabelBg0;
    QLabel *m_pLabelBg1;

    int m_currentFgIndex;
//    QLabel *m_pLabelFgArray[4];
    QLabel *m_pLabelFgTotal;
    CLabel *m_pLabelBtnArray[4];
    QToolButton *m_pButtonClose;
};

#endif // PREVIEW360_H
