#ifndef CLABEL_H
#define CLABEL_H

#include <QWidget>
#include <QtCore>
#include <QtGui>

class CLabel : public QWidget
{
    Q_OBJECT
public:
    explicit CLabel(QWidget *parent = 0);
    ~CLabel();

    void setPixmap(const QPixmap &);
    void setText(const QString &);
    void setMouseEnterFlag(bool);
    void setMousePressFlag(bool);

protected:
    void enterEvent(QEvent *);
    void leaveEvent(QEvent *);
    void mousePressEvent(QMouseEvent *);
    void paintEvent(QPaintEvent *);

signals:
    void signalLabelPress(CLabel *);

private:
    //functions
    void initVariable();
    void initSetupUi();
    void createFrame();
    void createWidget();
    void createLayout();
    void paintWidget(int, QPainter *);
    bool getMouseEnterFlag();
    bool getMousePressFlag();

    //members
    bool m_mouseEnterFlag;
    bool m_mousePressFlag;
    //pointer members
    QHBoxLayout *m_pHLayout;
    QLabel *m_pLabelIcon;
    QLabel *m_pLabelText;
};

#endif // CLABEL_H
