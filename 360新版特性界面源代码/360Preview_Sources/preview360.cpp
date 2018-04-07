#include "preview360.h"

Preview360::Preview360(QWidget *parent) :
    QFrame(parent)
{
    initVariable();
    initSetupUi();
}

Preview360::~Preview360()
{
    freeMemory();
}

void Preview360::paintEvent(QPaintEvent *)
{
    QBitmap bitmap(this->size());
    QPainter painter(&bitmap);
    painter.fillRect(bitmap.rect(), Qt::white);
    painter.setBrush(QColor(0, 0, 0));
    painter.drawRoundedRect(QRect(0, 2, this->width(), this->height()-2), 5, 5);
    painter.drawRoundedRect(QRect(20, 0, 120-20, 2), 2, 2);
    setMask(bitmap);
}

void Preview360::mousePressEvent(QMouseEvent *e)
{
    if (e->button() == Qt::LeftButton)
    {
        m_mouseSrcPos = e->pos();
        if (m_mouseSrcPos.y() <= 40)
        {
            m_mouseMoveWindowFlag = true;
        }
        else
        {
            m_currentFgXpos = m_pLabelFgTotal->x();
            m_mousePressFlag = true;
        }
    }
    else if (e->button() == Qt::RightButton)
    {
        if (getLabelMove())
        {
            if (m_currentFgIndex > 0)
            {
                m_currentFgIndex--;
                moveCurrentPage(false); //右移
            }
        }
    }
}

void Preview360::mouseReleaseEvent(QMouseEvent *e)
{
    int xpos = 0;

    if (m_mousePressFlag)
    {
        if (getLabelMove())
        {
            m_mouseDstPos = e->pos();

            xpos = m_mouseDstPos.x() - m_mouseSrcPos.x();

            if (xpos > 0)//右移
            {
                if (xpos >= WINDOW_ONEBUTTON_WIDTH)
                {
                    if (m_currentFgIndex > 0)
                    {
                        m_currentFgIndex--;
                        moveCurrentPage(false); //右移
                    }
                    else
                    {
                        moveCurrentPage(true); //左移
                    }
                }
                else
                {
                    moveCurrentPage(true); //左移
                }
            }
            else //左移
            {
                if (xpos <= -WINDOW_ONEBUTTON_WIDTH)
                {
                    if (m_currentFgIndex < WINDOW_PAGE_COUNT-1)
                    {
                        m_currentFgIndex++;
                        moveCurrentPage(true); //左移
                    }
                    else
                    {
                        moveCurrentPage(false); //右移
                    }
                }
                else
                {
                    moveCurrentPage(false); //右移
                }
            }

            m_mousePressFlag = false;
        }
    }
    else if (m_mouseMoveWindowFlag)
    {
        m_mouseMoveWindowFlag = false;
    }
}

void Preview360::mouseMoveEvent(QMouseEvent *e)
{
    int x = 0;

    if (m_mousePressFlag)
    {
        if (getLabelMove())
        {
            m_mouseDstPos = e->pos();
            x = m_mouseDstPos.x() - m_mouseSrcPos.x();

            setLabelMove(false);
            m_pLabelFgTotal->move(m_currentFgXpos + x, WINDOW_START_Y);
            setLabelMove(true);
        }
    }
    else if (m_mouseMoveWindowFlag)
    {
        m_mouseDstPos = e->pos();
        this->move(this->pos() + m_mouseDstPos - m_mouseSrcPos);
    }
}

void Preview360::keyPressEvent(QKeyEvent *e)
{
    if (getLabelMove())
    {
        switch(e->key())
        {
        case Qt::Key_Left:
        case Qt::Key_Up:
            if (m_currentFgIndex > 0)
            {
                m_currentFgIndex--;
                moveCurrentPage(false); //右移
            }
            break;

        case Qt::Key_Right:
        case Qt::Key_Down:
            if (m_currentFgIndex < WINDOW_PAGE_COUNT-1)
            {
                m_currentFgIndex++;
                moveCurrentPage(true); //左移
            }
            break;

        default:
            break;
        }
    }
}

bool Preview360::eventFilter(QObject *target, QEvent *event)
{
    EButtonMouseState state = EButtonMouseNone;

    if (target == m_pButtonClose)
    {
        if (event->type() == QEvent::Enter)
        {
            state = EButtonMouseEnter;
        }
        else if (event->type() == QEvent::Leave)
        {
            state = EButtonMouseDefault;
        }
        else if (((QMouseEvent *)event)->button() == Qt::LeftButton)
        {
            if (event->type() == QEvent::MouseButtonPress)
            {
                state = EButtonMousePress;
                //若点击在关闭按钮上,不拖动图像
                m_mousePressFlag = false;
            }
            else if (event->type() == QEvent::MouseButtonRelease)
            {
                state = EButtonMouseEnter;
            }
        }

        if (state != EButtonMouseNone)
        {
            setButtonIcon((QToolButton *)target, state);
        }
    }
    return QWidget::eventFilter(target, event);
}

void Preview360::slotLabelButtonPress(CLabel *label)
{
    for (int i = 0; i < WINDOW_BUTTON_COUNT; i++)
    {
        if (label != m_pLabelBtnArray[i])
        {
            m_pLabelBtnArray[i]->setMousePressFlag(false);
        }
    }
}

void Preview360::slotChangeCurrentPage(CLabel *label)
{
    int index = 0;

    for (int i = 0; i < WINDOW_PAGE_COUNT; i++)
    {
        if (label == m_pLabelBtnArray[i])
        {
            index = i;
            break;
        }
    }

    //移动的几种可能性,对于x坐标
    //index=0, 将label移动到-680*0
    //index=1, 将label移动到-680*1
    //index=2, 将label移动到-680*2
    //index=3, 将label移动到-680*3
    //点击左边的按钮 右移
    if (index < m_currentFgIndex)
    {
        while(index != m_currentFgIndex)
        {
            m_currentFgIndex--;
            moveCurrentPage(false);
        }
    }
    else if (index > m_currentFgIndex) //点击右边的按钮 左移
    {
        while(index != m_currentFgIndex)
        {
            m_currentFgIndex++;
            moveCurrentPage(true);
        }
    }
    else
    {
        //...
    }
}

void Preview360::initSetupUi()
{
    createFrame();
    createWidget();
    createEventFilter();
}

void Preview360::initVariable()
{
    m_mousePressFlag = false;
    m_mouseMoveWindowFlag = false;
    m_currentFgIndex = 0;
    m_currentFgXpos = 0;
}

void Preview360::createFrame()
{
    this->setWindowTitle(tr("360安全卫士 新版特性"));
    this->resize(QSize(WINDOW_WIDTH, WINDOW_HEIGHT));
    setWindowFlags(Qt::FramelessWindowHint);

    m_pLabelBg0 = new QLabel(this);
    m_pLabelBg0->setPixmap(QPixmap(":/images/bg_bottom.png"));
    m_pLabelBg0->setGeometry(QRect(0, 2, this->width(), this->height()-2));

    m_pLabelBg1 = new QLabel(this);
    m_pLabelBg1->setPixmap(QPixmap(":/images/bg_top.png"));
    m_pLabelBg1->setGeometry(QRect(0, 0, this->width(), this->height()));
}

void Preview360::createWidget()
{
    //将4张图片合成一张
    QPixmap pixmap(QSize(this->width()*WINDOW_PAGE_COUNT, WINDOW_HEIGHT-2));
    QPainter painter(&pixmap);
    for (int i = 0; i < WINDOW_PAGE_COUNT; i++)
    {
        painter.drawImage(QRect(WINDOW_WIDTH*i, 0, WINDOW_WIDTH, WINDOW_HEIGHT-2),
                          QImage(tr(":/images/desktop_%1.jpg").arg(i)));
    }
    m_pLabelFgTotal = new QLabel(this);

    m_pLabelFgTotal->resize(pixmap.size());
    m_pLabelFgTotal->setPixmap(pixmap);
    m_pLabelFgTotal->move(WINDOW_START_X, WINDOW_START_Y);

    QStringList nameList;
    nameList << tr("360安全桌面 ")
             << tr("木马防火墙   ")
             << tr("360保镖     ")
             << tr("电脑门诊     ");
    for (int i = 0; i < WINDOW_BUTTON_COUNT; i++)
    {
        CLabel *label = new CLabel(this);
        label = new CLabel(this);
        label->resize(QSize(155, 45));
        label->setPixmap(QPixmap(tr(":/images/btn_%1.png").arg(i)));
        label->setText(nameList.at(i));
        label->move(8+i*170, 319);
        connect(label, SIGNAL(signalLabelPress(CLabel*)), this, SLOT(slotLabelButtonPress(CLabel*)));;
        connect(label, SIGNAL(signalLabelPress(CLabel*)), this, SLOT(slotChangeCurrentPage(CLabel*)));
        m_pLabelBtnArray[i] = label;
    }
    m_pLabelBtnArray[0]->setMousePressFlag(true);

    m_pButtonClose = new QToolButton(this);
    m_pButtonClose->setFocusPolicy(Qt::NoFocus);
    m_pButtonClose->setStyleSheet("background:transparent;border:0px;");
    setButtonIcon(m_pButtonClose, EButtonMouseDefault);
    m_pButtonClose->move(QPoint(this->width()-52, 1));
    connect(m_pButtonClose, SIGNAL(clicked()), qApp, SLOT(quit()));

    //raise these widget
    m_pLabelBg1->raise();
    m_pButtonClose->raise();

    for (int i = 0; i < WINDOW_BUTTON_COUNT; i++)
    {
        m_pLabelBtnArray[i]->raise();
    }
}

void Preview360::createEventFilter()
{
    m_pButtonClose->installEventFilter(this);
}

void Preview360::setButtonIcon(QToolButton *btn, EButtonMouseState state)
{
    QPixmap pixmap(":/images/btn_close.png");
    int nWidth = pixmap.width()/4;
    int nHeight = pixmap.height();
    btn->setIcon(QIcon(pixmap.copy(QRect(state * nWidth, 0, nWidth, nHeight))));
    btn->setIconSize(QSize(nWidth, nHeight));
}

void Preview360::changeCurrentButton()
{
    for (int i = 0; i < WINDOW_BUTTON_COUNT; i++)
    {
        if (i != m_currentFgIndex)
        {
            m_pLabelBtnArray[i]->setMousePressFlag(false);
        }
        else
        {
            m_pLabelBtnArray[i]->setMousePressFlag(true);
        }
    }
}

inline void Preview360::setLabelMove(bool enable)
{
    m_labelMoveFlag = enable;
}

inline bool Preview360::getLabelMove()
{
    return m_labelMoveFlag;
}

void Preview360::moveCurrentPage(bool direction)
{
    int currentXpos = 0;//当前label的x坐标
    int destXpos = 0;//目标x坐标

    //改变当前页面对应的按钮
    changeCurrentButton();

    //图片的几个分割点
    //0-680, 680-1360, 1360-2040, 2040-2720
    //真:向左移;  假:向右移
    if (direction)
    {
        //左移的几种可能性,对于x坐标
        //index=0, 将label移动到-680*0
        //index=1, 将label移动到-680*1
        //index=2, 将label移动到-680*2
        //index=3, 将label移动到-680*3
        setLabelMove(false);
        currentXpos = m_pLabelFgTotal->x();
        destXpos = -WINDOW_WIDTH * m_currentFgIndex;
        while(currentXpos > destXpos)
        {
            m_pLabelFgTotal->move(currentXpos-WINDOW_PAGE_MOVE, WINDOW_START_Y);
            currentXpos = m_pLabelFgTotal->x();
            qApp->processEvents(QEventLoop::AllEvents);
        }
        m_pLabelFgTotal->move(destXpos, WINDOW_START_Y);
        setLabelMove(true);
    }
    else
    {
        //右移的几种可能性,对于x坐标,与左移一致
        //index=0, 将label移动到-680*0
        //index=1, 将label移动到-680*1
        //index=2, 将label移动到-680*2
        //index=3, 将label移动到-680*3
        setLabelMove(false);
        currentXpos = m_pLabelFgTotal->x();
        destXpos = -WINDOW_WIDTH * m_currentFgIndex;
        while(currentXpos < destXpos)
        {
            m_pLabelFgTotal->move(currentXpos+WINDOW_PAGE_MOVE, WINDOW_START_Y);
            currentXpos = m_pLabelFgTotal->x();
            qApp->processEvents(QEventLoop::AllEvents);
        }
        m_pLabelFgTotal->move(destXpos, WINDOW_START_Y);
        setLabelMove(true);
    }
}

void Preview360::freeMemory()
{
    for (int i = 0; i < WINDOW_PAGE_COUNT; i++)
    {
        delete m_pLabelBtnArray[i];
    }
    delete m_pLabelFgTotal;

    delete m_pButtonClose;
    delete m_pLabelBg0;
    delete m_pLabelBg1;
}
